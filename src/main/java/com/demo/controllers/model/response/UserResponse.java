package com.demo.controllers.model.response;

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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String email;
    private UserRole userRole;
    private AppStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date createdDate;
    public UserResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.userRole = user.getRole();
        this.status = user.getStatus();
        this.createdDate = user.getCreatedDate();
    }

}
