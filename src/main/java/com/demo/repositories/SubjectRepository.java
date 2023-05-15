package com.demo.repositories;


import com.demo.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Repository
public interface SubjectRepository extends JpaRepository<Subject, String>, JpaSpecificationExecutor<Subject> {
    List<Subject> findAllByStudentId(String id);
    @Modifying
    @Query("DELETE FROM Subject e WHERE e.studentId = :id")
    void deleteByStudentId(String id);

}
