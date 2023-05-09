package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.controllers.model.response.MemberDetailResponse;
import com.demo.entities.User;
import org.springframework.data.domain.Page;

/**
 * @author DiGiEx
 */
public interface UserService {
    User save(User user);

    User findById(String id);

    User getByEmailAndStatus(String email, AppStatus status);

    User getByIdAndNotINACTIVE(String id);

    Page<MemberDetailResponse> getPageMember(String searchKey, String sortField, boolean ascSort, int pageNumber, int pageSize);

}
