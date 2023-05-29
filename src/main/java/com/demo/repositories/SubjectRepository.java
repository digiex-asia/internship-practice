package com.demo.repositories;

import com.demo.common.enums.AppStatus;
import com.demo.entities.Subject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface SubjectRepository
    extends JpaRepository<Subject, String>, JpaSpecificationExecutor<Subject> {
  List<Subject> findAllByStudentId(String id);

  @Modifying
  @Query("DELETE FROM Subject e WHERE e.studentId = :id")
  void deleteByStudentId(String id);

  List<Subject> findAllByStudentIdInAndStatus(List<String> ids, AppStatus status);


  List<Subject> findAllByIdInAndStatus(List<String> ids, AppStatus status);

}
