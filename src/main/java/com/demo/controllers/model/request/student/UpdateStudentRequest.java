package com.demo.controllers.model.request.student;

import com.demo.common.enums.AppStatus;
import com.demo.common.enums.Gender;
import com.demo.common.utilities.Constant;
import com.demo.common.utilities.FormatDateJsonDeserializer;
import com.demo.common.utilities.ParamError;
import com.demo.controllers.model.request.subject.CreateSubjectRequest;
import com.demo.controllers.model.request.subject.UpdateSubjectRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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

    private Date bob;

    private String address;

    private Gender gender;

    private String phoneNumber;

    private String classId;

    List<UpdateSubjectRequest> subjects;
}
