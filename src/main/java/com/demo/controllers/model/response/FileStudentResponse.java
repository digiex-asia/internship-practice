package com.demo.controllers.model.response;

import com.demo.entities.Student;
import com.demo.entities.Subject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class FileStudentResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String dob;
    private String phoneNumber;
    private String gender;
    private Double math;
    private Double literature;
    private Double mediumScore;


    public FileStudentResponse(Student student, List<Subject> subjects) {
        this.email = student.getEmail();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.dob = String.valueOf(student.getBob());
        this.phoneNumber = student.getPhoneNumber();
        this.gender = String.valueOf(student.getGender());
        Subject subjectMath = subjects.stream().filter(i -> Objects.equals(i.getName(), "Math")).findAny().orElse(null);
        Subject subjectliterature = subjects.stream().filter(i -> Objects.equals(i.getName(), "Literature")).findAny().orElse(null);
        Double sum = 0.0;
        if (subjectMath != null) {
            this.math = subjectMath.getScore();
            sum += subjectMath.getScore();
        }
        if (subjectliterature != null) {
            this.literature = subjectliterature.getScore();
            sum += subjectliterature.getScore();

        }

        this.mediumScore = sum / 2;


    }


    boolean containsName(List<Subject> list, String name) {
        return list.stream().anyMatch(p -> p.getName().equals(name));
    }
}
