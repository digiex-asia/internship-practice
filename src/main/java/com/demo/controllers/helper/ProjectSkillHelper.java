package com.demo.controllers.helper;

import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.controllers.model.request.UpdateSkillHistoryRequest;
import com.demo.controllers.model.request.UpdateSkillRequest;
import com.demo.entities.ProjectSkill;
import com.demo.entities.Skill;
import com.demo.entities.WorkHistorySkill;
import com.demo.services.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectSkillHelper {
    final private SkillService skillService;

    public List<ProjectSkill> createProjectSkill(List<ProjectSkill> projectSkills, List<String> idSkillsForProject, String workHistoryProjectId) {
        for (String idSkill : idSkillsForProject) {
            ProjectSkill projectSkill = new ProjectSkill();
            projectSkill.setId(UniqueID.getUUID());
            projectSkill.setWorkHistoryProjectId(workHistoryProjectId);
            projectSkill.setSkillId(idSkill);
            projectSkills.add(projectSkill);
        }
        return projectSkills;
    }

    public List<ProjectSkill> updateWorkHistorySkills(List<ProjectSkill> projectSkills, List<UpdateSkillHistoryRequest> skillRequests, String id) {
        List<UpdateSkillHistoryRequest> addSkills = skillRequests.stream().filter(e -> e.getId() == null || e.getId().trim().isEmpty()).collect(Collectors.toList());
        List<UpdateSkillHistoryRequest> updateSkills = skillRequests.stream().filter(e -> e.getId() != null).collect(Collectors.toList());
        if (!addSkills.isEmpty()) {
            validationAdd(addSkills);
            for (UpdateSkillHistoryRequest workHistoryRequest : addSkills) {

                ProjectSkill projectSkill = new ProjectSkill();
                projectSkill.setId(UniqueID.getUUID());
                projectSkill.setWorkHistoryProjectId(id);
                projectSkill.setSkillId(workHistoryRequest.getSkillId());
                projectSkills.add(projectSkill);
            }
        }
        if (!updateSkills.isEmpty()) {
            updateSkills.forEach(updateSkillRequest -> {
                projectSkills.forEach(workHistorySkill -> {
                    if (workHistorySkill.getId().equals(updateSkillRequest.getId())) {

                        if (updateSkillRequest.getSkillId() != null && !updateSkillRequest.getSkillId().trim().isEmpty()) {
                            workHistorySkill.setSkillId(updateSkillRequest.getSkillId());
                        }
                    }
                });
            });
        }

        List<Skill> skillUsers = skillService.findAllByIdIn(projectSkills.stream()
                .map(ProjectSkill::getSkillId).distinct().collect(Collectors.toList()));
        System.out.println(skillUsers.size());
        System.out.println(projectSkills.size());
        if (skillUsers.size() != projectSkills.size()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Project Skill is not found or duplicate");
        }
        return projectSkills;
    }

    private void validationAdd(List<UpdateSkillHistoryRequest> addSkills) {
        Optional<UpdateSkillHistoryRequest> skillId = addSkills.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getSkillId() == null || updateSubjectRequest.getSkillId().trim().isEmpty()).findAny();
        if (skillId.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Skill id is null");
        }
    }
}
