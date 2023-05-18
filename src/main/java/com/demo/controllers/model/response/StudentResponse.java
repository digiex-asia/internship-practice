package com.demo.controllers.model.response;

import com.demo.common.enums.AppStatus;
import com.demo.common.enums.Gender;
import com.demo.common.utilities.Constant;
import com.demo.entities.Student;
import com.demo.entities.Subject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE)
    private Date bob;
    private String address;
    private Gender gender;
    private String phoneNumber;
    private AppStatus status;
    private String classId;
    private Integer countSubject;
    private List<Subject> subjects;
    private Double avgScore;

    public StudentResponse(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.email = student.getEmail();
        this.bob = student.getBob();
        this.address = student.getAddress();
        this.gender = student.getGender();
        this.phoneNumber = student.getPhoneNumber();
        this.status = student.getStatus();
        this.classId = student.getClassId();

    }
    public StudentResponse(Student student,Double avgScore) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.email = student.getEmail();
        this.bob = student.getBob();
        this.address = student.getAddress();
        this.gender = student.getGender();
        this.phoneNumber = student.getPhoneNumber();
        this.status = student.getStatus();
        this.classId = student.getClassId();
        this.avgScore=avgScore;
    }
    public StudentResponse(Student student, List<Subject> subjects) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.email = student.getEmail();
        this.bob = student.getBob();
        this.address = student.getAddress();
        this.gender = student.getGender();
        this.phoneNumber = student.getPhoneNumber();
        this.status = student.getStatus();
        this.subjects = subjects;
        this.classId = student.getClassId();
        this.countSubject = subjects.size();
    }
    public StudentResponse(Student student, List<Subject> subjects, Double avgScore) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.email = student.getEmail();
        this.bob = student.getBob();
        this.address = student.getAddress();
        this.gender = student.getGender();
        this.phoneNumber = student.getPhoneNumber();
        this.status = student.getStatus();
        this.subjects = subjects;
        this.classId = student.getClassId();
        this.countSubject = subjects.size();
        this.avgScore = avgScore;
    }
}
