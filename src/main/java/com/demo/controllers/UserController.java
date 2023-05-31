package com.demo.controllers;

import com.demo.common.enums.AppStatus;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.RestAPIResponse;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.common.utilities.Validator;
import com.demo.controllers.helper.*;
import com.demo.controllers.model.request.*;

import com.demo.controllers.model.response.*;
import com.demo.entities.*;
import com.demo.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author DigiEx Group
 */
@RestController
@RequestMapping(ApiPath.USER_API)
@Slf4j
@RequiredArgsConstructor
public class UserController extends AbstractBaseController {
    final private UserService userService;
    final private WorkHistoryService workHistoryService;
    final private SkillService skillService;
    final private UserSkillService userSkillService;
    final private WorkHistoryProjectService workHistoryProjectService;
    final private UserHelper userHelper;
    final private EducationHelper educationHelper;
    final private UserSkillHelper userSkillHelper;
    final private EducationService educationService;
    final private WorkHistoryHelper workHistoryHelper;
    final private WorkHistorySkillService workHistorySkillService;
    final private WorkHistoryProjectHelper workHistoryProjectHelper;
    final private ProjectSkillService projectSkillService;
    final private ProjectSkillHelper projectSkillHelper;
    final private WorkHistorySkillHelper workHistorySkillHelper;

    @PostMapping()
    public ResponseEntity<RestAPIResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        //check time start always < time end
        // check skill phai co trong he thong
        Validator.validatePhone(createUserRequest.getPhoneNumber());
        Validator.validateEmail(createUserRequest.getEmail());

        User userByEmail = userService.getByEmail(createUserRequest.getEmail());
        Validator.mustNull(userByEmail, RestAPIStatus.EXISTED, "Email existed");
        User userByPhone = userService.getByPhoneNumber(createUserRequest.getPhoneNumber());
        Validator.mustNull(userByPhone, RestAPIStatus.EXISTED, "Phone existed");
        User newUser = userHelper.createUser(createUserRequest);


        List<String> nameEducations = createUserRequest.getEducations().stream().map(CreateEducationRequest::getUniversityCollege)
                .map(String::toLowerCase).distinct().collect(Collectors.toList());
        if (nameEducations.size() != createUserRequest.getEducations().size()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "College name is duplicated");
        }


        List<String> skillIdUsers = createUserRequest.getSkills().stream().map(CreateSkillRequest::getSkillId).distinct().collect(Collectors.toList());
        List<Skill> skills = skillService.findAllByIdIn(skillIdUsers);

        if (skills.size() != createUserRequest.getSkills().size()) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Skill is duplicated");
        }
        List<UserSkill> userSkills = userSkillHelper.createUserSkill(createUserRequest.getSkills(), newUser.getId());


        List<Education> educations = new ArrayList<>();

        List<WorkHistory> workHistories = new ArrayList<>();
        List<WorkHistoryProject> workHistoryProjects = new ArrayList<>();
        List<WorkHistorySkill> workHistorySkills = new ArrayList<>();
        List<ProjectSkill> projectSkills = new ArrayList<>();


        if (createUserRequest.getWorkHistories() != null && !createUserRequest.getWorkHistories().isEmpty()) {

            for (CreateWorkHistoryRequest createWorkHistoryRequest : createUserRequest.getWorkHistories()) {
                if (createWorkHistoryRequest.getWorkFrom() > new Date().getTime() && createWorkHistoryRequest.getWorkTo() > new Date().getTime() &&
                        createWorkHistoryRequest.getWorkFrom() >= createWorkHistoryRequest.getWorkTo()) {
                    throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "WorkFrom or WorkTo invalid");
                }
                WorkHistory workHistory = new WorkHistory();
                workHistory.setId(UniqueID.getUUID());
                workHistory.setCompany(createWorkHistoryRequest.getCompany());
                workHistory.setStatus(AppStatus.ACTIVE);
                workHistory.setWorkFrom(createWorkHistoryRequest.getWorkFrom());
                workHistory.setRegion(createWorkHistoryRequest.getRegion());
                workHistory.setDescription(createWorkHistoryRequest.getDescription());
                workHistory.setWorkTo(createWorkHistoryRequest.getWorkTo());
                workHistory.setPosition(createWorkHistoryRequest.getPosition());
                workHistory.setUserId(newUser.getId());
                if (createWorkHistoryRequest.getSkillIds() != null && !createWorkHistoryRequest.getSkillIds().isEmpty()) {
                    List<Skill> skillWorkHistories = skillService.findAllByIdIn(createWorkHistoryRequest.getSkillIds().stream().distinct().collect(Collectors.toList()));
                    if (skillWorkHistories.size() != createWorkHistoryRequest.getSkillIds().size()) {
                        throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Skill is not found");
                    }
                    for (String idSkill : createWorkHistoryRequest.getSkillIds()) {
                        WorkHistorySkill workHistorySkill = new WorkHistorySkill();
                        workHistorySkill.setId(UniqueID.getUUID());
                        workHistorySkill.setWorkHistoryId(workHistory.getId());
                        workHistorySkill.setSkillId(idSkill);
                        workHistorySkills.add(workHistorySkill);
                    }
                }
                if (createWorkHistoryRequest.getWorkHistoryProjects() != null && !createWorkHistoryRequest.getWorkHistoryProjects().isEmpty()) {

                    for (CreateWorkHistoryProjectRequest workHistoryProjectRequest : createWorkHistoryRequest.getWorkHistoryProjects()) {
                        if (workHistoryProjectRequest.getStartDate() > new Date().getTime() && workHistoryProjectRequest.getEndDate() > new Date().getTime() &&
                                workHistoryProjectRequest.getStartDate() >= workHistoryProjectRequest.getEndDate()) {
                            throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "StartDate or EndDate invalid");
                        }
                        WorkHistoryProject workHistoryProject = new WorkHistoryProject();
                        workHistoryProject.setId(UniqueID.getUUID());
                        workHistoryProject.setStatus(AppStatus.ACTIVE);
                        workHistoryProject.setName(workHistoryProjectRequest.getName().trim());
                        workHistoryProject.setDescription(workHistoryProjectRequest.getDescription().trim());
                        workHistoryProject.setEndDate(workHistoryProjectRequest.getEndDate());
                        workHistoryProject.setStartDate(workHistoryProjectRequest.getStartDate());
                        workHistoryProject.setWorkHistoryId(workHistory.getId());
                        workHistoryProjects.add(workHistoryProject);
                        if (workHistoryProjectRequest.getSkillIds() != null && !workHistoryProjectRequest.getSkillIds().isEmpty()) {
                            List<Skill> skillWorkHistoryProjects = skillService.findAllByIdIn(workHistoryProjectRequest.getSkillIds()
                                    .stream().distinct().collect(Collectors.toList()));
                            if (skillWorkHistoryProjects.size() != workHistoryProjectRequest.getSkillIds().size()) {
                                throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "Skill is not found");
                            }
                            for (String idSkill : workHistoryProjectRequest.getSkillIds()) {
                                ProjectSkill projectSkill = new ProjectSkill();
                                projectSkill.setId(UniqueID.getUUID());
                                projectSkill.setWorkHistoryProjectId(workHistoryProject.getId());
                                projectSkill.setSkillId(idSkill);
                                projectSkills.add(projectSkill);
                            }
                        }
                    }
                }
                workHistories.add(workHistory);
            }
        }
        if (createUserRequest.getEducations() != null) {
            for (CreateEducationRequest createEducationRequest : createUserRequest.getEducations()) {
                if (createEducationRequest.getStartDate() > new Date().getTime() && createEducationRequest.getEndDate() > new Date().getTime() &&
                        createEducationRequest.getStartDate() >= createEducationRequest.getEndDate()) {
                    throw new ApplicationException(RestAPIStatus.BAD_REQUEST, "StartDate or EndDate invalid");
                }
                educations.add(educationHelper.createEducation(createEducationRequest, newUser.getId()));
            }
        }

        List<WorkHistoryDTO> workHistoryDTOS = workHistoryHelper.toDto(workHistories, workHistorySkills, projectSkills, workHistoryProjects);
        projectSkillService.saveAll(projectSkills);
        workHistorySkillService.saveAll(workHistorySkills);
        userSkillService.saveAll(userSkills);
        workHistoryService.saveAll(workHistories);
        workHistoryProjectService.saveAll(workHistoryProjects);
        educationService.saveAll(educations);
        userService.save(newUser);
        return responseUtil.successResponse(new UserResponse(newUser, educations, userSkills, workHistoryDTOS));
    }


    @PutMapping(ApiPath.ID)
    public ResponseEntity<RestAPIResponse> updateUser(@PathVariable(name = "id") String id, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        System.out.println(updateUserRequest);
        User user = userService.findById(id);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "Student Not Found");
        if (updateUserRequest.getPhoneNumber() != null) {
            Validator.validatePhone(updateUserRequest.getPhoneNumber());
        }
        if (updateUserRequest.getEmail() != null) {
            Validator.validateEmail(updateUserRequest.getEmail());
        }


        User userByEmail = userService.getByEmail(updateUserRequest.getEmail());
        Validator.mustNull(userByEmail, RestAPIStatus.EXISTED, "Email existed");
        User userByPhone = userService.getByPhoneNumber(updateUserRequest.getPhoneNumber());
        Validator.mustNull(userByPhone, RestAPIStatus.EXISTED, "Phone existed");
        user = userHelper.updateUser(user, updateUserRequest);
        List<WorkHistory> workHistories = workHistoryService.findAllByUserId(user.getId());
        if (updateUserRequest.getWorkHistories() != null) {
            workHistories = workHistoryHelper.updateWorkHistory(updateUserRequest.getWorkHistories(), workHistories, user.getId());
        }

        List<UserSkill> userSkills = userSkillService.findAllByUserId(user.getId());

        if (updateUserRequest.getSkills() != null) {
            userSkills = userSkillHelper.updateSkill(userSkills, updateUserRequest.getSkills(), user.getId());
        }

        List<Education> educations = educationService.findAllByUserId(user.getId());

        if (updateUserRequest.getEducations() != null) {
            educations = educationHelper.updateEducation(updateUserRequest.getEducations(), educations, user.getId());
        }

        List<WorkHistoryDTO> workHistoryDTOS = workHistoryHelper.toDto(workHistories);
        workHistoryService.saveAll(workHistories);
        userSkillService.saveAll(userSkills);
        educationService.saveAll(educations);
        userService.save(user);
        return responseUtil.successResponse(new UserResponse(user, educations, userSkills, workHistoryDTOS));
    }

    @GetMapping(ApiPath.ID)
    public ResponseEntity<RestAPIResponse> getUserDetail(@PathVariable(name = "id") String id) {
        UserResponse user = userService.getById(id);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "User Not Found");

        if (user != null) {
            List<UserSkill> userSkills = userSkillService.findAllByUserId(user.getId());
            List<Education> educations = educationService.findAllByUserId(user.getId());
            List<WorkHistory> workHistories = workHistoryService.findAllByUserId(user.getId());
            List<WorkHistorySkill> workHistorySkills = workHistorySkillService.findAllByWorkHistoryIdIn(workHistories.stream().map(WorkHistory::getId).collect(Collectors.toList()));
            List<WorkHistoryProject> workHistoryProjects = workHistoryProjectService.findAllByWorkHistoryIdIn(workHistories.stream().map(WorkHistory::getId).collect(Collectors.toList()));
            List<ProjectSkill> projectSkills = projectSkillService.findAllByWorkHistoryProjectIdIn(workHistoryProjects.stream().map(WorkHistoryProject::getId).collect(Collectors.toList()));

            user.setUserSkills(userSkills.stream()
                    .filter(userSkill -> user.getId().equals(userSkill.getUserId()))
                    .collect(Collectors.toList()));
            user.setEducations(educations.stream()
                    .filter(edu -> user.getId().equals(edu.getUserId()))
                    .collect(Collectors.toList()));
            List<WorkHistoryDTO> workHistoryDTOS = workHistoryHelper.toDto(workHistories, workHistorySkills, projectSkills, workHistoryProjects, user.getId());
            user.setWorkHistories(workHistoryDTOS);

        }
        return responseUtil.successResponse(user);
    }
    @GetMapping(ApiPath.ALL)
    public ResponseEntity<RestAPIResponse> getAllUser() {
        List<UserResponse> users = userService.getAll();
        if (!users.isEmpty()) {
            List<String> userIds = users.stream().map(UserResponse::getId).collect(Collectors.toList());
            List<UserSkill> userSkills = userSkillService.findAllByUserIdIn(userIds);
            List<Education> educations = educationService.findAllByUserIdIn(userIds);
            List<WorkHistory> workHistories = workHistoryService.findAllByUserIdIn(userIds);
            List<WorkHistorySkill> workHistorySkills = workHistorySkillService.findAllByWorkHistoryIdIn(workHistories.stream().map(WorkHistory::getId).collect(Collectors.toList()));
            List<WorkHistoryProject> workHistoryProjects = workHistoryProjectService.findAllByWorkHistoryIdIn(workHistories.stream().map(WorkHistory::getId).collect(Collectors.toList()));
            List<ProjectSkill> projectSkills = projectSkillService.findAllByWorkHistoryProjectIdIn(workHistoryProjects.stream().map(WorkHistoryProject::getId).collect(Collectors.toList()));
            for (UserResponse user : users) {
                user.setUserSkills(userSkills.stream()
                        .filter(userSkill -> user.getId().equals(userSkill.getUserId()))
                        .collect(Collectors.toList()));
                user.setEducations(educations.stream()
                        .filter(edu -> user.getId().equals(edu.getUserId()))
                        .collect(Collectors.toList()));
                List<WorkHistoryDTO> workHistoryDTOS = workHistoryHelper.toDto(workHistories, workHistorySkills, projectSkills, workHistoryProjects, user.getId());
                user.setWorkHistories(workHistoryDTOS);
            }
        }
        return responseUtil.successResponse(users);
    }
    @DeleteMapping(ApiPath.ID)
    public ResponseEntity<RestAPIResponse> deleteUser(@PathVariable(name = "id") String id) {
        System.out.println(1);
        User user = userService.findById(id);
        Validator.notNull(user, RestAPIStatus.NOT_FOUND, "User Not Found");
        List<Education> educations = educationService.findAllByUserId(user.getId());
        List<WorkHistory> workHistories = workHistoryService.findAllByUserId(user.getId());
        List<WorkHistoryProject> workHistoryProjects = workHistoryProjectService.findAllByWorkHistoryIdIn(workHistories.stream().map(WorkHistory::getId).collect(Collectors.toList()));
        user.setStatus(AppStatus.INACTIVE);
        educations.forEach(e -> e.setStatus(AppStatus.INACTIVE));
        workHistories.forEach(e -> e.setStatus(AppStatus.INACTIVE));
        workHistoryProjects.forEach(e -> e.setStatus(AppStatus.INACTIVE));
        userService.save(user);
        educationService.saveAll(educations);
        workHistoryProjectService.saveAll(workHistoryProjects);
        workHistoryService.saveAll(workHistories);
        educationService.saveAll(educations);
        return responseUtil.successResponse(user);
    }
}
