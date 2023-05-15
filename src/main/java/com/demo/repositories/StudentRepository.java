package com.demo.repositories;

import com.demo.common.enums.AppStatus;
import com.demo.controllers.model.response.StudentResponse;
import com.demo.entities.Session;
import com.demo.entities.Student;
import com.demo.entities.User;
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

public interface StudentRepository extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {

    @Query("select p from Student  p")
    List<Student> findAllById(String id);

    Student getByEmail(String email);

    Student getByPhoneNumber(String phoneNumber);

    List<Student> findAllByClassId(String id);


    @Query("select new com.demo.controllers.model.response.StudentResponse(student) from Student student where  student.classId=:classId  and  (:phone is null or student.phoneNumber = :phone) and (:email is null or student.email = :email) and( student.lastName like :searchKey or student.firstName like :searchKey)")
    Page<StudentResponse> getStudentPaging(
            @Param(value = "email") String email,
            @Param(value = "phone") String phone,
            @Param(value = "classId") String classId,
            @Param(value = "searchKey") String searchKey,
            Pageable pageable);


    List<Student> findAllByIdAndStatus(String id, AppStatus status);
}
