package com.demo.controllers.model.request;

import com.demo.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserRequest {
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String firstName;
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String lastName;

    private String phoneNumber;
    @Size(max = 20, message = ParamError.MAX_LENGTH)
    private String passwordHash;
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String email;
    List<UpdateEducationRequest> educations;
    List<UpdateSkillRequest> skills;
    List<UpdateWorkHistoryRequest> workHistories;
}
