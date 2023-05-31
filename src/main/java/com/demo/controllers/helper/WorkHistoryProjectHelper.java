package com.demo.controllers.helper;

import com.demo.common.enums.AppStatus;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.controllers.model.request.CreateWorkHistoryProjectRequest;
import com.demo.controllers.model.request.UpdateSkillRequest;
import com.demo.controllers.model.request.UpdateWorkHistoryProjectRequest;
import com.demo.controllers.model.request.UpdateWorkHistoryRequest;
import com.demo.entities.ProjectSkill;
import com.demo.entities.Skill;
import com.demo.entities.WorkHistoryProject;
import com.demo.entities.WorkHistorySkill;
import com.demo.services.ProjectSkillService;
import com.demo.services.SkillService;
import com.demo.services.WorkHistoryProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WorkHistoryProjectHelper {
    final private ProjectSkillHelper projectSkillHelper;
    final private ProjectSkillService projectSkillService;
    final private WorkHistoryProjectService workHistoryProjectService;
    final private SkillService skillService;

    public WorkHistoryProject createWorkHistoryProject(CreateWorkHistoryProjectRequest createRequestWorkHistoryProject, String workHistoryId) {
        WorkHistoryProject workHistoryProject = new WorkHistoryProject();
        workHistoryProject.setId(UniqueID.getUUID());
        workHistoryProject.setStatus(AppStatus.ACTIVE);
        workHistoryProject.setName(createRequestWorkHistoryProject.getName());
        workHistoryProject.setDescription(createRequestWorkHistoryProject.getDescription());
        workHistoryProject.setEndDate(createRequestWorkHistoryProject.getEndDate());
        workHistoryProject.setStartDate(createRequestWorkHistoryProject.getStartDate());
        workHistoryProject.setWorkHistoryId(workHistoryId);
        return workHistoryProject;
    }

    public WorkHistoryProject createWorkHistoryProject(UpdateWorkHistoryProjectRequest updateWorkHistoryProjectRequest, String workHistoryId) {
        WorkHistoryProject workHistoryProject = new WorkHistoryProject();
        workHistoryProject.setId(UniqueID.getUUID());
        workHistoryProject.setStatus(AppStatus.ACTIVE);
        workHistoryProject.setName(updateWorkHistoryProjectRequest.getName());
        workHistoryProject.setDescription(updateWorkHistoryProjectRequest.getDescription());
        workHistoryProject.setEndDate(updateWorkHistoryProjectRequest.getEndDate());
        workHistoryProject.setStartDate(updateWorkHistoryProjectRequest.getStartDate());
        workHistoryProject.setWorkHistoryId(workHistoryId);
        return workHistoryProject;
    }

    public List<WorkHistoryProject> updateWorkHistoryProject(List<WorkHistoryProject> workHistoryProjects, List<UpdateWorkHistoryProjectRequest> workHistoryProjectRequests,
                                                             String workHistoryId) {
        List<UpdateWorkHistoryProjectRequest> addWorkHistoryProjects = workHistoryProjectRequests.stream().filter(e -> e.getId() == null || e.getId().trim().isEmpty()).collect(Collectors.toList());
        List<UpdateWorkHistoryProjectRequest> updateWorkHistoryProjects = workHistoryProjectRequests.stream().filter(e -> e.getId() != null).collect(Collectors.toList());


        if (!addWorkHistoryProjects.isEmpty()) {
            validationAdd(addWorkHistoryProjects);
            for (UpdateWorkHistoryProjectRequest workHistoryRequest : addWorkHistoryProjects) {
                if (workHistoryRequest.getStartDate() > new Date().getTime() && workHistoryRequest.getEndDate() > new Date().getTime() &&
                        workHistoryRequest.getStartDate() >= workHistoryRequest.getEndDate()) {
                    throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "StartDate or EndDate invalid");
                }
                workHistoryProjects.add(createWorkHistoryProject(workHistoryRequest, workHistoryId));
            }
        }
        List<ProjectSkill> newProjectSkills = new ArrayList<>();
        if (!updateWorkHistoryProjects.isEmpty()) {
            updateWorkHistoryProjects.forEach(workHistoryProjectRequest -> workHistoryProjects.forEach(workHistoryProject -> {
                if (workHistoryProject.getId().equals(workHistoryProjectRequest.getId())) {
                    if (AppStatus.INACTIVE.equals(workHistoryProjectRequest.getStatus())) {
                        workHistoryProject.setStatus(AppStatus.INACTIVE);
                    } else {

                        if (workHistoryProjectRequest.getName() != null && !workHistoryProjectRequest.getName().trim().isEmpty()) {
                            workHistoryProject.setName(workHistoryProjectRequest.getName());
                        }
                        if (workHistoryProjectRequest.getDescription() != null && !workHistoryProjectRequest.getDescription().trim().isEmpty()) {
                            workHistoryProject.setDescription(workHistoryProjectRequest.getDescription());
                        }
                        if (workHistoryProjectRequest.getStartDate() != null) {
                            workHistoryProject.setStartDate(workHistoryProjectRequest.getStartDate());
                        }
                        if (workHistoryProjectRequest.getEndDate() != null) {
                            workHistoryProject.setEndDate(workHistoryProjectRequest.getEndDate());
                        }
                        List<ProjectSkill> projectSkills = projectSkillService.findAllByWorkHistoryProjectId(workHistoryProject.getId());

                        if (workHistoryProjectRequest.getSkills() != null) {
                            if (workHistoryProjectService.findById(workHistoryProjectRequest.getId()) == null) {
                                throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Work History Project not found");

                            }
                            newProjectSkills.addAll(projectSkillHelper.updateWorkHistorySkills(projectSkills, workHistoryProjectRequest.getSkills(), workHistoryProjectRequest.getId()));
                        }
                        if (workHistoryProject.getStartDate() > new Date().getTime() && workHistoryProject.getEndDate() > new Date().getTime() &&
                                workHistoryProject.getStartDate() >= workHistoryProject.getEndDate()) {
                            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "StartDate or EndDate invalid");
                        }

                        projectSkillService.saveAll(projectSkills);
                    }

                }
            }));
        }
        return workHistoryProjects;
    }

    private void validationAdd(List<UpdateWorkHistoryProjectRequest> addWorkHistories) {
        Optional<UpdateWorkHistoryProjectRequest> description = addWorkHistories.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getDescription() == null || updateSubjectRequest.getDescription().trim().isEmpty()).findAny();
        if (description.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Description is null");
        }

        Optional<UpdateWorkHistoryProjectRequest> name = addWorkHistories.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getName() == null || updateSubjectRequest.getName().trim().isEmpty()).findAny();
        if (name.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Name is null");
        }
        Optional<UpdateWorkHistoryProjectRequest> workTo = addWorkHistories.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getEndDate() == null).findAny();
        if (workTo.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "End date is null");
        }
        Optional<UpdateWorkHistoryProjectRequest> workFrom = addWorkHistories.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getStartDate() == null || updateSubjectRequest.getStartDate() > new Date().getTime()).findAny();
        if (workFrom.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Start date is null or Start date doesn't > time now");
        }
    }
}
