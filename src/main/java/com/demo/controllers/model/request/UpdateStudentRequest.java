package com.demo.controllers.model.request;

import com.demo.common.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateStudentRequest {
    private String firstName;
    private String lastName;
    private String email;
    private Long bob;
    private String address;
    private Gender gender;
    private String phoneNumber;
    List<UpdateSubjectRequest> subjects;
}
