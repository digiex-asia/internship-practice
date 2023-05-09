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
public class MemberDetailResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole userRole;

    private String nameAvatar;

    private AppStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date updatedDate;
    public MemberDetailResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.userRole = user.getRole();
        this.nameAvatar = getNameOfAvatar(user.getFirstName(),user.getLastName());
        this.status = user.getStatus();
        this.createdDate = user.getCreatedDate();
        this.updatedDate = user.getUpdatedDate();
    }
    private String getNameOfAvatar(String firstName, String lastName){
        String nameOfAvatar = "";
        if(firstName != null && !firstName.isEmpty()){
            nameOfAvatar += String.valueOf(firstName.charAt(0));
        }
        if(lastName != null && !lastName.isEmpty()){
            nameOfAvatar += String.valueOf(lastName.charAt(0));
        }

        return nameOfAvatar;
    }
}
