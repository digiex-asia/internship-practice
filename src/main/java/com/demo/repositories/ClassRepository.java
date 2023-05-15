package com.demo.repositories;

import com.demo.common.enums.AppStatus;
import com.demo.controllers.model.response.ClassDetailsResponse;
import com.demo.controllers.model.response.ClassResponse;
import com.demo.entities.Class;
import com.demo.entities.Session;
import com.demo.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ClassRepository extends JpaRepository<Class, String>, JpaSpecificationExecutor<Class> {
    Class getByName(String name);

    @Query("select new com.demo.controllers.model.response.ClassResponse(class_) from Class class_ where class_.status =:status and class_.name like :searchKey")
    Page<ClassResponse> getUserPaging(
            @Param(value = "status") AppStatus status,
            @Param(value = "searchKey") String searchKey,
            Pageable pageable);


    List<Class> findAllByStatus(AppStatus status);

    Class findByIdAndStatus(String id, AppStatus active);
}
