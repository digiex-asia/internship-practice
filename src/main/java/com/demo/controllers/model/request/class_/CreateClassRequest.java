package com.demo.controllers.model.request.class_;

import com.demo.common.enums.AppStatus;
import com.demo.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateClassRequest {
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String name;
    @Max(value = 20, message = ParamError.MAX_VALUE)
    @Min(value = 1, message = ParamError.MIN_VALUE)
    private Integer max_student;

}
