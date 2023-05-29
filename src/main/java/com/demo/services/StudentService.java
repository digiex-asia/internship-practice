package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.common.enums.Gender;
import com.demo.common.enums.TypeRank;
import com.demo.controllers.model.response.StudentResponse;
import com.demo.entities.Student;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

public interface StudentService {

  Student getByEmail(String email);

  Student getByPhoneNumber(String phoneNumber);

  void save(Student newStudent);

  Student findById(String id);

  List<Student> findAllByClassId(String id);

  Page<StudentResponse> getPageMember(
      String email,
      String phone,
      String ClassId,
      String searchKey,
      String sortField,
      boolean ascSort,
      int pageNumber,
      int pageSize);

  void saveAll(List<Student> students);

  List<Student> findAllByStatus(AppStatus active);

  List<StudentResponse> getTop3StudentByType(TypeRank type);

  List<Student> findAll();

  List<Student> getAllByClassIdIn(List<String> ids);

  Page<Student> getStudentPage(
      String classId,
      Gender gender,
      String firstName,
      String lastName,
      String email,
      Long startDate,
      Long endDate,
      String searchKey,
      String sortField,
      boolean ascSort,
      int pageNumber,
      int pageSize);


  List<StudentResponse> getStudents(String classId);
}
