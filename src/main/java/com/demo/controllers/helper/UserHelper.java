package com.demo.controllers.helper;

import com.demo.common.enums.*;
import com.demo.common.utilities.UniqueID;
import com.demo.entities.User;
import com.demo.controllers.model.request.*;
import org.springframework.stereotype.Component;

/**
 * @author DigiEx Group
 */
@Component
public class UserHelper {


    public User createMemberAdmin(CreateMemberAdmin createMemberAdmin) {

        User user = new User();
        user.setId(UniqueID.getUUID());
        user.setStatus(AppStatus.PENDING);
        user.setFirstName(createMemberAdmin.getFirstName().trim());
        user.setLastName(createMemberAdmin.getLastName().trim());
        user.setEmail(createMemberAdmin.getEmail().trim());
        user.setRole(createMemberAdmin.getRole());

        return user;
    }






}
