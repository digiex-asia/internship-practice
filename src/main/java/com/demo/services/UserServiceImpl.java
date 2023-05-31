package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.common.utilities.Constant;
import com.demo.controllers.model.response.UserResponse;
import com.demo.entities.User;
import com.demo.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DigiEx Group
 */
@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(String id) {
        return userRepository.findByIdAndStatusIsNot(id, AppStatus.INACTIVE);
    }

    @Override
    public User getByIdAndNotINACTIVE(String id) {
        return userRepository.findByIdAndStatusNot(id, AppStatus.INACTIVE);
    }

    @Override
    public Page<UserResponse> getPageMember(String searchKey, String sortField, boolean ascSort, int pageNumber, int pageSize) {
        String properties = "";
        switch (sortField) {
            case Constant.MEMBER_FIRST_NAME:
                properties = "firstName";
                break;
            case Constant.MEMBER_LAST_NAME:
                properties = "lastName";
                break;
            case Constant.MEMBER_EMAIL:
                properties = "email";
                break;
            default:
                properties = "createdDate";
                break;
        }
        Sort.Direction direction = ascSort ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, properties);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return userRepository.getUserPaging(AppStatus.INACTIVE, "%" + searchKey + "%", pageable);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, AppStatus.ACTIVE);
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumberAndStatus(phoneNumber, AppStatus.ACTIVE);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAllByStatus(AppStatus.ACTIVE);
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.getAll(AppStatus.ACTIVE);
    }

    @Override
    public UserResponse getById(String id) {
        return userRepository.findByIdAndStatus(id,AppStatus.ACTIVE);
    }

    @Override
    public User getByEmailAndStatus(String email, AppStatus status) {
        return userRepository.findByEmailAndStatus(email, AppStatus.ACTIVE);
    }


}
