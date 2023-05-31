package com.demo.controllers.model.response;

import com.demo.entities.Education;
import com.demo.entities.UserSkill;
import com.demo.entities.WorkHistory;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.demo.common.enums.AppStatus;
import com.demo.common.enums.UserRole;
import com.demo.common.utilities.Constant;
import com.demo.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String email;
    private String phone;

    private UserRole userRole;
    private AppStatus status;

    private Long createdDate;

    private List<UserSkill> userSkills;
    private List<WorkHistoryDTO> workHistories;
    private List<Education> educations;
    public UserResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.userRole = user.getRole();
        this.status = user.getStatus();
        this.phone = user.getPhoneNumber();
        this.createdDate = user.getCreatedDate();
    }

    public UserResponse(User user, List<Education> educations, List<UserSkill> userSkills ,List<WorkHistoryDTO> workHistories) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.userRole = user.getRole();
        this.status = user.getStatus();
        this.phone = user.getPhoneNumber();

        this.createdDate = user.getCreatedDate();
        this.educations = educations;
        this.userSkills = userSkills;
        this.workHistories=workHistories;
    }



}
