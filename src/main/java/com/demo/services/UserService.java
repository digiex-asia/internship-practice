package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.controllers.model.response.UserResponse;
import com.demo.entities.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author DiGiEx
 */
public interface UserService {
    void save(User user);

    User findById(String id);

    User getByEmailAndStatus(String email, AppStatus status);

    User getByIdAndNotINACTIVE(String id);

    Page<UserResponse> getPageMember(String searchKey, String sortField, boolean ascSort, int pageNumber, int pageSize);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    List<User> findAll();

    List<UserResponse> getAll();

    UserResponse getById(String id);
}
