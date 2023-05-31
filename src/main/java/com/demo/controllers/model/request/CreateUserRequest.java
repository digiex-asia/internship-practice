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
public class CreateUserRequest {
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String firstName;

    @Size(max = 64, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String lastName;

    @Size(max = 12, message = ParamError.MAX_VALUE)
    @Size(min = 8, message = ParamError.MIN_VALUE)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String phoneNumber;
    @Size(max = 15, message = ParamError.MAX_VALUE)
    @Size(min = 6, message = ParamError.MIN_VALUE)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String passwordHash;
    @Size(max = 250, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String email;
    @NotEmpty(message = ParamError.FIELD_NAME)
    @Valid List<CreateEducationRequest> educations;

    @NotEmpty(message = ParamError.FIELD_NAME)
    @Valid  List<CreateSkillRequest> skills;
    @Valid List<CreateWorkHistoryRequest> workHistories;
}
