package com.demo.repositories;

import com.demo.common.enums.AppStatus;
import com.demo.controllers.model.response.StudentResponse;
import com.demo.entities.Student;
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
public interface StudentRepository
    extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {

  @Query("select p from Student  p")
  List<Student> findAllById(String id);

  Student getByEmail(String email);

  Student getByPhoneNumber(String phoneNumber);

  List<Student> findAllByClassId(String id);

  @Query(
      "select new com.demo.controllers.model.response.StudentResponse(student,AVG(subject.score))  from Student student  join Subject subject on student.id=subject.studentId where student.status='ACTIVE' and subject.status='ACTIVE'  and student.classId=:classId  and  (:phone is null or student.phoneNumber = :phone) and (:email is null or student.email = :email) and( student.lastName like :searchKey or student.firstName like :searchKey) GROUP  BY subject.studentId ORDER BY AVG(subject.score) DESC ")
  Page<StudentResponse> getStudentPagingDesc(
      @Param(value = "email") String email,
      @Param(value = "phone") String phone,
      @Param(value = "classId") String classId,
      @Param(value = "searchKey") String searchKey,
      Pageable pageable);

  @Query(
      "select new com.demo.controllers.model.response.StudentResponse(student,AVG(subject.score))  from Student student  join Subject subject on student.id=subject.studentId where  student.status='ACTIVE' and subject.status='ACTIVE' and student.classId=:classId  and  (:phone is null or student.phoneNumber = :phone) and (:email is null or student.email = :email) and( student.lastName like :searchKey or student.firstName like :searchKey) GROUP  BY subject.studentId ORDER BY AVG(subject.score) ASC ")
  Page<StudentResponse> getStudentPagingAsc(
      @Param(value = "email") String email,
      @Param(value = "phone") String phone,
      @Param(value = "classId") String classId,
      @Param(value = "searchKey") String searchKey,
      Pageable pageable);

  @Query(
      "select new com.demo.controllers.model.response.StudentResponse(student) from Student student where student.status='ACTIVE'  and  student.classId=:classId  and  (:phone is null or student.phoneNumber = :phone) and (:email is null or student.email = :email) and( student.lastName like :searchKey or student.firstName like :searchKey)")
  Page<StudentResponse> getStudentPaging(
      @Param(value = "email") String email,
      @Param(value = "phone") String phone,
      @Param(value = "classId") String classId,
      @Param(value = "searchKey") String searchKey,
      Pageable pageable);

  List<Student> findAllByIdAndStatus(String id, AppStatus status);

  List<Student> findAllByStatus(AppStatus active);

  Student findByIdAndStatus(String id, AppStatus active);

  List<Student> findAllByClassIdInAndStatus(List<String> ids, AppStatus status);

  @Query(
      "select new com.demo.controllers.model.response.StudentResponse(student,AVG(subject.score)) "
          + " from Student student join Subject subject on student.id = subject.studentId where "
          + "student.status =:status and subject.status =: status and student.classId=:classId GROUP  BY subject.studentId ORDER BY AVG(subject.score) DESC ")
  List<StudentResponse> getStudents(
      @Param(value = "classId") String classId,
      @Param(value = "status") AppStatus status);


  @Query(
          "select new com.demo.controllers.model.response.StudentResponse(student,AVG(subject.score)) "
                  + " from Student student join Subject subject on student.id = subject.studentId where "
                  + "student.status =:status and subject.status =: status  GROUP  BY subject.studentId ORDER BY AVG(subject.score) DESC, student.bob DESC ")
  List<StudentResponse> getAllStudent(@Param(value = "status") AppStatus status);



}
