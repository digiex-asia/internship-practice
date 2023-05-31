package com.demo.repositories;

import com.demo.common.enums.AppStatus;
import com.demo.entities.Education;
import com.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface EducationRepository extends JpaRepository<Education, String>, JpaSpecificationExecutor<Education> {


    List<Education> findAllByUserIdAndStatus(String userId, AppStatus active);

    List<Education> findAllByUserIdInAndStatus(List<String> userIds, AppStatus active);
}
