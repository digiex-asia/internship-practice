package com.demo.controllers.model.request;

import com.demo.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateEducationRequest {
    @Min(value = 2,message = ParamError.FIELD_NAME)
    private long endDate;

    @Min(value = 1,message = ParamError.FIELD_NAME)
    private long startDate;
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String major;

    @Size(max = 64, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String universityCollege;
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String degree;

}
