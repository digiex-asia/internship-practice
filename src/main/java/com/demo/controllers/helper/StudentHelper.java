package com.demo.controllers.helper;

import com.demo.common.enums.AppStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.controllers.model.request.class_.CreateClassRequest;
import com.demo.controllers.model.request.student.CreateStudentRequest;
import com.demo.controllers.model.request.student.UpdateStudentRequest;
import com.demo.controllers.model.response.StudentResponse;
import com.demo.entities.Class;
import com.demo.entities.Student;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class StudentHelper {
    public Student createStudent(CreateStudentRequest studentRequest) {


        Student student = new Student();
        student.setId(UniqueID.getUUID());
        student.setStatus(AppStatus.ACTIVE);
        student.setFirstName(studentRequest.getFirstName().trim());
        student.setLastName(studentRequest.getLastName().trim());
        student.setEmail(studentRequest.getEmail().trim());
        student.setAddress(studentRequest.getAddress().trim());
        student.setBob(studentRequest.getBob());
        student.setGender(studentRequest.getGender());
        student.setClassId(studentRequest.getClassId());
        student.setPhoneNumber(studentRequest.getPhoneNumber());


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
            student.setEmail(studentRequest.getEmail());

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
        if (studentRequest.getClassId() != null && !studentRequest.getClassId().trim().isEmpty()) {
            student.setClassId(studentRequest.getClassId());

        }
        if (studentRequest.getPhoneNumber() != null && !studentRequest.getPhoneNumber().trim().isEmpty()) {
            student.setPhoneNumber(studentRequest.getPhoneNumber());

        }

        return student;
    }

    public List<StudentResponse> sortList(List<StudentResponse> listStudent) {
        listStudent.sort((p1, p2) -> {
            if (p1.getAvgScore() > p2.getAvgScore()) {
                return -1;
            }
            if (p1.getAvgScore() < p2.getAvgScore()) {
                return 1;
            }
            if (p1.getBob().compareTo(p2.getBob()) < 0) {
                return -1;
            }
            if (p1.getBob().compareTo(p2.getBob()) > 0) {
                return 1;
            }
            return -p1.getAvgScore().compareTo(p2.getAvgScore());
        });
        if (listStudent.size() >= 3) {
            return listStudent.subList(0, 3);
        }
        return listStudent;
    }
}
