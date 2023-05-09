package com.demo.controllers.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.demo.common.enums.UserRole;
import com.demo.common.utilities.ParamError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateMemberAdmin {

    @Size(max = 64, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String firstName;

    @Size(max = 64, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String lastName;

    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 250, message = ParamError.MAX_LENGTH)
    private String email;

    @NotNull(message = ParamError.FIELD_NAME)
    private UserRole role;


}
