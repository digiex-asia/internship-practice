package com.demo.repositories;

import com.demo.common.enums.*;
import com.demo.controllers.model.response.MemberDetailResponse;
import com.demo.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author DiGiEx Group
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {


    User findByIdAndStatusIsNot(String id, AppStatus status);

    User findByEmailAndStatus(String email, AppStatus status);

    User findByIdAndStatusNot(String id, AppStatus appStatus);

    @Query("select new com.demo.controllers.model.response.MemberDetailResponse(user) from User user where user.status <> :status and (user.firstName like :searchKey or  user.lastName like :searchKey)")
    Page<MemberDetailResponse> getUserPaging(@Param(value = "status") AppStatus status,
                                             @Param(value = "searchKey") String searchKey,
                                             Pageable pageable);

}
