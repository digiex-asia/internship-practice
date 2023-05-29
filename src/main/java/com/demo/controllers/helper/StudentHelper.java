package com.demo.controllers.helper;

import com.demo.common.enums.AppStatus;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.common.utilities.Validator;
import com.demo.controllers.model.request.CreateStudentRequest;
import com.demo.controllers.model.request.UpdateStudentRequest;
import com.demo.controllers.model.request.UpdateSubjectRequest;
import com.demo.controllers.model.response.StudentResponse;
import com.demo.entities.Student;
import java.util.List;
import java.util.Optional;

import com.demo.services.StudentService;
import org.springframework.stereotype.Component;

@Component
public class StudentHelper {
  public Student createStudent(CreateStudentRequest studentRequest) {
    Student student = new Student();
    student.setId(UniqueID.getUUID());
    student.setStatus(AppStatus.ACTIVE);
    student.setFirstName(studentRequest.getFirstName().trim());
    student.setLastName(studentRequest.getLastName().trim());
    student.setEmail(studentRequest.getEmail().trim());
    if (studentRequest.getAddress() != null) {
      student.setAddress(studentRequest.getAddress().trim());
    }
    student.setBob(studentRequest.getBob());
    student.setGender(studentRequest.getGender());
    student.setClassId(studentRequest.getClassId().trim());
    student.setPhoneNumber(studentRequest.getPhoneNumber().trim());
    return student;
  }

  public Student updateStudent(Student student, UpdateStudentRequest studentRequest, StudentService studentService) {

    if (studentRequest.getFirstName() != null && !studentRequest.getFirstName().trim().isEmpty()) {
      student.setFirstName(studentRequest.getFirstName().trim());
    }
    if (studentRequest.getLastName() != null && !studentRequest.getLastName().trim().isEmpty()) {
      student.setFirstName(studentRequest.getLastName());
    }
    if (studentRequest.getEmail() != null && !studentRequest.getEmail().trim().isEmpty() &&
            !studentRequest.getEmail().trim().equals(student.getEmail())) {
      Validator.validateEmail(studentRequest.getEmail().trim());
      Student studentByEmail = studentService.getByEmail(studentRequest.getEmail().trim());
      Validator.mustNull(studentByEmail, RestAPIStatus.EXISTED, "Student existed");

      student.setEmail(studentRequest.getEmail().trim());
    }
    if (studentRequest.getAddress() != null && !studentRequest.getAddress().trim().isEmpty()) {
      student.setAddress(studentRequest.getAddress());
    }
    if (studentRequest.getBob() != null) {
      student.setBob(studentRequest.getBob());
    }
    if (studentRequest.getGender() != null) {
      student.setGender(studentRequest.getGender());
    }

    if (studentRequest.getPhoneNumber() != null && !studentRequest.getPhoneNumber().trim().isEmpty() &&
            !studentRequest.getPhoneNumber().trim().equals(student.getPhoneNumber())) {
      Validator.validatePhone(studentRequest.getPhoneNumber().trim());

      Student studentByPhone =
              studentService.getByPhoneNumber(studentRequest.getPhoneNumber().trim());
      Validator.mustNull(studentByPhone, RestAPIStatus.EXISTED, "Student existed");
      student.setPhoneNumber(studentRequest.getPhoneNumber().trim());
    }
    return student;
  }

}
