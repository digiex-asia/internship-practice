package com.demo.controllers.helper;

import com.demo.common.enums.*;
import com.demo.common.utilities.UniqueID;
import com.demo.entities.User;
import com.demo.controllers.model.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author DigiEx Group
 */
@Component
@RequiredArgsConstructor
public class UserHelper {

    final private PasswordEncoder passwordEncoder;

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


    public User createUser(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setId(UniqueID.getUUID());
        user.setStatus(AppStatus.ACTIVE);
        user.setFirstName(createUserRequest.getFirstName().trim());
        user.setLastName(createUserRequest.getLastName().trim());
        user.setEmail(createUserRequest.getEmail().trim());
        user.setPhoneNumber(createUserRequest.getPhoneNumber());
        user.setPasswordHash(passwordEncoder.encode(createUserRequest.getPasswordHash()));
        user.setRole(UserRole.DEVELOPER);

        return user;
    }

    public User updateUser(User user, UpdateUserRequest updateUserRequest) {
        if (updateUserRequest.getFirstName() != null && !updateUserRequest.getFirstName().trim().isEmpty()) {
            user.setFirstName(updateUserRequest.getFirstName().trim());

        }
        if (updateUserRequest.getLastName() != null && !updateUserRequest.getLastName().trim().isEmpty()) {
            user.setLastName(updateUserRequest.getLastName().trim());

        }
        if (updateUserRequest.getEmail() != null && !updateUserRequest.getEmail().trim().isEmpty()) {
            user.setEmail(updateUserRequest.getEmail().trim());

        }
        if (updateUserRequest.getPhoneNumber() != null && !updateUserRequest.getPhoneNumber().trim().isEmpty()) {
            user.setPhoneNumber(updateUserRequest.getPhoneNumber());

        }
        if (updateUserRequest.getPasswordHash() != null && !updateUserRequest.getPasswordHash().trim().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(updateUserRequest.getPasswordHash()));

        }


        return user;
    }
}
