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

  public Student updateStudent(Student student, UpdateStudentRequest studentRequest) {

    if (studentRequest.getFirstName() != null && !studentRequest.getFirstName().trim().isEmpty()) {
      student.setFirstName(studentRequest.getFirstName().trim());
    }
    if (studentRequest.getLastName() != null && !studentRequest.getLastName().trim().isEmpty()) {
      student.setFirstName(studentRequest.getLastName());
    }
    if (studentRequest.getEmail() != null && !studentRequest.getEmail().trim().isEmpty()) {
      Validator.validateEmail(studentRequest.getEmail().trim());
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

    if (studentRequest.getPhoneNumber() != null
        && !studentRequest.getPhoneNumber().trim().isEmpty()) {
      Validator.validatePhone(studentRequest.getPhoneNumber().trim());
      student.setPhoneNumber(studentRequest.getPhoneNumber().trim());
    }
    return student;
  }

}
