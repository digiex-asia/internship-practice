package com.demo.repositories;

import com.demo.common.enums.AppStatus;
import com.demo.controllers.model.response.ClassResponse;
import com.demo.entities.Class;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ClassRepository
    extends JpaRepository<Class, String>, JpaSpecificationExecutor<Class> {
  Class findFirstByNameAndStatus(String name, AppStatus status);

  @Query("select new com.demo.controllers.model.response.ClassResponse(c) from Class c where c.status =:status and c.name like :searchKey")
  Page<ClassResponse> getUserPaging(
      @Param(value = "status") AppStatus status,
      @Param(value = "searchKey") String searchKey,
      Pageable pageable);

  List<Class> findAllByStatus(AppStatus status);

  Class findByIdAndStatus(String id, AppStatus active);

  @Query("select new com.demo.controllers.model.response.ClassResponse(c) from Class c where c.status =:status")
  List<ClassResponse> getAllClass(@Param(value = "status") AppStatus status);


}
