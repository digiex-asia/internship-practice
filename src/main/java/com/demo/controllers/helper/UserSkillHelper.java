package com.demo.controllers.helper;

import com.demo.common.enums.AppStatus;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.controllers.model.request.CreateSkillRequest;
import com.demo.controllers.model.request.UpdateEducationRequest;
import com.demo.controllers.model.request.UpdateSkillHistoryRequest;
import com.demo.controllers.model.request.UpdateSkillRequest;
import com.demo.entities.Education;
import com.demo.entities.Skill;
import com.demo.entities.UserSkill;
import com.demo.entities.WorkHistorySkill;
import com.demo.services.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserSkillHelper {
    final private SkillService skillService;

    public List<UserSkill> createUserSkill(List<CreateSkillRequest> skillRequests, String userId) {

        List<UserSkill> userSkills = new ArrayList<>();
        for (CreateSkillRequest skillRequest : skillRequests) {
            UserSkill userSkill = new UserSkill();
            userSkill.setId(UniqueID.getUUID());
            userSkill.setUserId(userId);
            userSkill.setSkillId(skillRequest.getSkillId());
            userSkill.setExperience(skillRequest.getExperience());
            userSkills.add(userSkill);
        }
        return userSkills;
    }

    public List<UserSkill> updateSkill(List<UserSkill> userSkills, List<UpdateSkillRequest> skillRequests, String id) {
        List<UpdateSkillRequest> addSkills = skillRequests.stream().filter(e -> e.getId() == null || e.getId().trim().isEmpty()).collect(Collectors.toList());
        List<UpdateSkillRequest> updateSkills = skillRequests.stream().filter(e -> e.getId() != null).collect(Collectors.toList());
        if (!addSkills.isEmpty()) {
            validationAdd(addSkills);

            for (UpdateSkillRequest skillRequest : addSkills) {
                UserSkill userSkill = new UserSkill();
                userSkill.setId(UniqueID.getUUID());
                userSkill.setUserId(id);
                userSkill.setSkillId(skillRequest.getSkillId());
                userSkill.setExperience(skillRequest.getExperience());
                userSkills.add(userSkill);
            }
        }
        if (!updateSkills.isEmpty()) {

            updateSkills.forEach(updateSkillRequest -> {
                userSkills.forEach(workHistorySkill -> {
                    if (workHistorySkill.getId().equals(updateSkillRequest.getId())) {
                        if (updateSkillRequest.getSkillId() != null && !updateSkillRequest.getSkillId().trim().isEmpty() ) {
                            workHistorySkill.setSkillId(updateSkillRequest.getSkillId());
                            workHistorySkill.setExperience(updateSkillRequest.getExperience());
                        }
                    }
                });
            });
        }
        List<Skill> skillUsers = skillService.findAllByIdIn(userSkills.stream()
                .map(UserSkill::getSkillId).distinct().collect(Collectors.toList()));
        if (skillUsers.size() != userSkills.size()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "User Skill is not found or duplicated");
        }
        return userSkills;
    }

    private void validationAdd(List<UpdateSkillRequest> addSkills) {


        Optional<UpdateSkillRequest> id = addSkills.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getSkillId() == null || updateSubjectRequest.getSkillId().trim().isEmpty()).findAny();
        if (id.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Skill id is null");
        }

    }


}
