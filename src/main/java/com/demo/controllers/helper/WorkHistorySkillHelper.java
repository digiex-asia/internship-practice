package com.demo.controllers.helper;

import com.demo.common.enums.AppStatus;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.controllers.model.request.CreateSkillRequest;
import com.demo.controllers.model.request.UpdateSkillHistoryRequest;
import com.demo.controllers.model.request.UpdateSkillRequest;
import com.demo.entities.Skill;
import com.demo.entities.UserSkill;
import com.demo.entities.WorkHistoryProject;
import com.demo.entities.WorkHistorySkill;
import com.demo.services.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WorkHistorySkillHelper {
    final private SkillService skillService;

    public List<WorkHistorySkill> createWorkHistorySkill(List<WorkHistorySkill> workHistorySkills, List<String> idSkillsForWorkHistory, String workHistoryId) {
        for (String idSkill : idSkillsForWorkHistory) {
            WorkHistorySkill workHistorySkill = new WorkHistorySkill();
            workHistorySkill.setId(UniqueID.getUUID());
            workHistorySkill.setWorkHistoryId(workHistoryId);
            workHistorySkill.setSkillId(idSkill);
            workHistorySkills.add(workHistorySkill);
        }
        return workHistorySkills;
    }
    public List<WorkHistorySkill> updateWorkHistorySkills(List<WorkHistorySkill> workHistorySkills, List<UpdateSkillHistoryRequest> workHistorySkillRequests,
                                                          String id) {
        List<UpdateSkillHistoryRequest> addSkills = workHistorySkillRequests.stream().filter(e -> e.getId() == null || e.getId().trim().isEmpty()).collect(Collectors.toList());
        List<UpdateSkillHistoryRequest> updateSkills = workHistorySkillRequests.stream().filter(e -> e.getId() != null).collect(Collectors.toList());
        if (!addSkills.isEmpty()) {

            validationAdd(addSkills);
            for (UpdateSkillHistoryRequest workHistoryRequest : addSkills) {
                WorkHistorySkill workHistorySkill = new WorkHistorySkill();
                workHistorySkill.setId(UniqueID.getUUID());
                workHistorySkill.setWorkHistoryId(id);
                workHistorySkill.setSkillId(workHistoryRequest.getSkillId());
                workHistorySkills.add(workHistorySkill);
            }
        }
        if (!updateSkills.isEmpty()) {
            updateSkills.forEach(updateSkillRequest -> {
                workHistorySkills.forEach(workHistorySkill -> {
                    if (workHistorySkill.getId().equals(updateSkillRequest.getId())) {

                        if (updateSkillRequest.getSkillId() != null && !updateSkillRequest.getSkillId().trim().isEmpty() ) {
                            workHistorySkill.setSkillId(updateSkillRequest.getSkillId());
                        }
                    }
                });
            });
        }

        List<Skill> skillUsers = skillService.findAllByIdIn(workHistorySkills.stream()
                .map(WorkHistorySkill::getSkillId).distinct().collect(Collectors.toList()));
        if (skillUsers.size() != workHistorySkills.size()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Work History Skill is not found or duplicate");
        }
        return workHistorySkills;
    }


    private void validationAdd(List<UpdateSkillHistoryRequest> addSkills) {
        Optional<UpdateSkillHistoryRequest> skillId = addSkills.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getSkillId() == null || updateSubjectRequest.getSkillId().trim().isEmpty()).findAny();
        if (skillId.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Skill id is null");
        }
    }

}
