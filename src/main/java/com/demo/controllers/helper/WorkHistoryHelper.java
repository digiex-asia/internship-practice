package com.demo.controllers.helper;

import com.demo.common.enums.AppStatus;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.common.utilities.Validator;
import com.demo.controllers.model.request.*;
import com.demo.controllers.model.response.ProjectSkillDTO;
import com.demo.controllers.model.response.WorkHistoryDTO;
import com.demo.controllers.model.response.WorkHistoryProjectDTO;
import com.demo.controllers.model.response.WorkHistorySkillDTO;
import com.demo.entities.*;
import com.demo.services.SkillService;
import com.demo.services.WorkHistoryProjectService;
import com.demo.services.WorkHistorySkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WorkHistoryHelper {
    final private WorkHistoryProjectHelper workHistoryProjectHelper;
    final private WorkHistoryProjectService workHistoryProjectService;

    final private WorkHistorySkillService workHistorySkillService;
    final private WorkHistorySkillHelper workHistorySkillHelper;
    final private SkillService skillService;

    public WorkHistory createWorkHistory(CreateWorkHistoryRequest createWorkHistoryRequest, String userId) {
        WorkHistory workHistory = new WorkHistory();
        workHistory.setId(UniqueID.getUUID());
        workHistory.setCompany(createWorkHistoryRequest.getCompany());
        workHistory.setStatus(AppStatus.ACTIVE);
        workHistory.setWorkFrom(createWorkHistoryRequest.getWorkFrom());
        workHistory.setRegion(createWorkHistoryRequest.getRegion());
        workHistory.setDescription(createWorkHistoryRequest.getDescription());
        workHistory.setWorkTo(createWorkHistoryRequest.getWorkTo());
        workHistory.setPosition(createWorkHistoryRequest.getPosition());
        workHistory.setUserId(userId);
        return workHistory;
    }

    public WorkHistory createWorkHistory(UpdateWorkHistoryRequest updateWorkHistoryRequest, String userId) {
        WorkHistory workHistory = new WorkHistory();
        workHistory.setId(UniqueID.getUUID());
        workHistory.setCompany(updateWorkHistoryRequest.getCompany());
        workHistory.setStatus(AppStatus.ACTIVE);
        workHistory.setWorkFrom(updateWorkHistoryRequest.getWorkFrom());
        workHistory.setRegion(updateWorkHistoryRequest.getRegion());
        workHistory.setDescription(updateWorkHistoryRequest.getDescription());
        workHistory.setWorkTo(updateWorkHistoryRequest.getWorkTo());
        workHistory.setPosition(updateWorkHistoryRequest.getPosition());
        workHistory.setUserId(userId);
        return workHistory;
    }

    public List<WorkHistory> updateWorkHistory(List<UpdateWorkHistoryRequest> updateWorkHistoryRequests, List<WorkHistory> workHistories, String userId) {
        List<UpdateWorkHistoryRequest> addWorkHistories = updateWorkHistoryRequests.stream().filter(e -> e.getId() == null || e.getId().trim().isEmpty()).collect(Collectors.toList());
        List<UpdateWorkHistoryRequest> updateWorkHistories = updateWorkHistoryRequests.stream().filter(e -> e.getId() != null).collect(Collectors.toList());

        if (!addWorkHistories.isEmpty()) {
            validationAdd(addWorkHistories);
            for (UpdateWorkHistoryRequest workHistoryRequest : addWorkHistories) {
                if (workHistoryRequest.getWorkFrom() > new Date().getTime() && workHistoryRequest.getWorkTo() > new Date().getTime() &&
                        workHistoryRequest.getWorkFrom() >= workHistoryRequest.getWorkTo()) {
                    throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "WorkFrom or WorkTo invalid");
                }
                workHistories.add(createWorkHistory(workHistoryRequest, userId));
            }
        }
        if (!updateWorkHistories.isEmpty()) {
            updateWorkHistories.forEach(workHistoryRequest -> workHistories.forEach(workHistory -> {
                if (workHistory.getId().equals(workHistoryRequest.getId())) {
                    if (AppStatus.INACTIVE.equals(workHistoryRequest.getStatus())) {
                        workHistory.setStatus(AppStatus.INACTIVE);
                    } else {

                        if (workHistoryRequest.getCompany() != null && !workHistoryRequest.getCompany().trim().isEmpty()) {
                            workHistory.setCompany(workHistoryRequest.getCompany());
                        }
                        if (workHistoryRequest.getDescription() != null && !workHistoryRequest.getDescription().trim().isEmpty()) {
                            workHistory.setDescription(workHistoryRequest.getDescription());
                        }
                        if (workHistoryRequest.getPosition() != null && !workHistoryRequest.getPosition().trim().isEmpty()) {
                            workHistory.setPosition(workHistoryRequest.getPosition());
                        }
                        if (workHistoryRequest.getRegion() != null && !workHistoryRequest.getRegion().trim().isEmpty()) {
                            workHistory.setRegion(workHistoryRequest.getRegion());
                        }
                        if (workHistoryRequest.getWorkFrom() != null) {
                            workHistory.setWorkFrom(workHistoryRequest.getWorkFrom());
                        }
                        if (workHistoryRequest.getWorkTo() != null) {
                            workHistory.setWorkTo(workHistoryRequest.getWorkTo());
                        }
                        List<WorkHistorySkill> workHistorySkills = workHistorySkillService.findAllByWorkHistoryId(workHistory.getId());
                        List<WorkHistoryProject> workHistoryProject = workHistoryProjectService.findAllByWorkHistoryId(workHistory.getId());
                        if (workHistoryRequest.getSkillIds() != null && !workHistoryRequest.getSkillIds().isEmpty()) {
                            if (workHistoryProjectService.findById(workHistoryRequest.getId()) == null) {
                                throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Work History not found");

                            }
                            workHistorySkills = workHistorySkillHelper.updateWorkHistorySkills(workHistorySkills, workHistoryRequest.getSkillIds(), workHistoryRequest.getId());
                        }
                        if (workHistoryRequest.getWorkHistoryProjects() != null) {

                            if (workHistoryProjectService.findById(workHistoryRequest.getId()) == null) {
                                throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Work History not found");
                            }
                            workHistoryProject = workHistoryProjectHelper.updateWorkHistoryProject(workHistoryProject, workHistoryRequest.getWorkHistoryProjects(), workHistoryRequest.getId());
                        }

                        if (workHistory.getWorkFrom() > new Date().getTime() && workHistory.getWorkTo() > new Date().getTime() &&
                                workHistory.getWorkFrom() >= workHistory.getWorkTo()) {
                            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "WorkFrom or WorkTo invalid");
                        }

                        List<WorkHistoryProject> workHistoryProjectList = workHistoryProject.stream().filter(subject -> AppStatus.ACTIVE.equals(subject.getStatus())).collect(Collectors.toList());
                        List<String> names = workHistoryProjectList.stream().map(WorkHistoryProject::getName).map(String::toLowerCase).distinct().collect(Collectors.toList());
                        if (names.size() != workHistoryProjectList.size()) {
                            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Project name is duplicated");
                        }
                        System.out.println(workHistorySkills);
                        workHistorySkillService.saveAll(workHistorySkills);
                        workHistoryProjectService.saveAll(workHistoryProject);
                    }

                }
            }));
        }

        List<WorkHistory> workHistoryList = workHistories.stream().filter(subject -> AppStatus.ACTIVE.equals(subject.getStatus())).collect(Collectors.toList());
        List<String> names = workHistoryList.stream().map(WorkHistory::getCompany).map(String::toLowerCase).distinct().collect(Collectors.toList());
        Validator.checkListDuplicated(names, workHistoryList, "Company");

        return workHistories;
    }


    private void validationAdd(List<UpdateWorkHistoryRequest> addWorkHistories) {
        Optional<UpdateWorkHistoryRequest> company = addWorkHistories.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getCompany() == null || updateSubjectRequest.getCompany().trim().isEmpty()).findAny();
        if (company.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Company is null");
        }

        Optional<UpdateWorkHistoryRequest> description = addWorkHistories.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getDescription() == null || updateSubjectRequest.getDescription().trim().isEmpty()).findAny();
        if (description.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Description is null");
        }
        Optional<UpdateWorkHistoryRequest> region = addWorkHistories.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getRegion() == null || updateSubjectRequest.getRegion().trim().isEmpty()).findAny();
        if (region.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Region is null");
        }

        Optional<UpdateWorkHistoryRequest> position = addWorkHistories.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getPosition() == null || updateSubjectRequest.getPosition().trim().isEmpty()).findAny();
        if (position.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Position is null");
        }

        Optional<UpdateWorkHistoryRequest> workTo = addWorkHistories.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getWorkTo() == null).findAny();
        if (workTo.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "WorkTo is null");
        }
        Optional<UpdateWorkHistoryRequest> workFrom = addWorkHistories.stream()
                .filter(updateSubjectRequest -> updateSubjectRequest.getWorkFrom() == null || updateSubjectRequest.getWorkFrom() > new Date().getTime()).findAny();
        if (workFrom.isPresent()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Work From is null or Work From time doesn't > time now");
        }
    }

    public List<WorkHistoryDTO> toDto(List<WorkHistory> workHistories, List<WorkHistorySkill> workHistorySkills, List<ProjectSkill> projectSkills, List<WorkHistoryProject> workHistoryProjects) {
        return workHistories.stream()
                .map(entry -> new WorkHistoryDTO(entry.getId(), entry.getCompany(), entry.getDescription(),
                        entry.getRegion(), entry.getPosition(), entry.getWorkFrom(), entry.getWorkTo(), entry.getStatus(), entry.getUserId(),
                        workHistoryProjects.stream()
                                .map(e -> new WorkHistoryProjectDTO(e.getId(), e.getDescription(), e.getEndDate(), e.getStartDate(),
                                        e.getName(), e.getStatus(), e.getWorkHistoryId(),
                                        projectSkills.stream().map(
                                                l -> new ProjectSkillDTO(l.getId(), l.getSkillId(), l.getWorkHistoryProjectId())).collect(Collectors.toList())
                                )).collect(Collectors.toList()),
                        workHistorySkills.stream().map(
                                k -> new WorkHistorySkillDTO(k.getId(), k.getWorkHistoryId(), k.getSkillId())).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public List<WorkHistoryDTO> toDto(List<WorkHistory> workHistories) {
        return new ArrayList<>();
    }

    public List<WorkHistoryDTO> toDto(List<WorkHistory> workHistories, List<WorkHistorySkill> workHistorySkills, List<ProjectSkill> projectSkills, List<WorkHistoryProject> workHistoryProjects, String userId) {
        return workHistories.stream().filter(u -> u.getUserId().equals(userId))
                .map(entry -> new WorkHistoryDTO(entry.getId(), entry.getCompany(), entry.getDescription(),
                        entry.getRegion(), entry.getPosition(), entry.getWorkFrom(), entry.getWorkTo(), entry.getStatus(), entry.getUserId(),
                        workHistoryProjects.stream().filter(whp -> whp.getWorkHistoryId().equals(entry.getId()))
                                .map(e -> new WorkHistoryProjectDTO(e.getId(), e.getDescription(), e.getEndDate(), e.getStartDate(),
                                        e.getName(), e.getStatus(), e.getWorkHistoryId(),
                                        projectSkills.stream().filter(ps -> ps.getWorkHistoryProjectId().equals(e.getId())).map(
                                                l -> new ProjectSkillDTO(l.getId(), l.getSkillId(), l.getWorkHistoryProjectId())).collect(Collectors.toList())
                                )).collect(Collectors.toList()),
                        workHistorySkills.stream().filter(whs -> whs.getWorkHistoryId().equals(entry.getId())).map(
                                k -> new WorkHistorySkillDTO(k.getId(), k.getWorkHistoryId(), k.getSkillId())).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
