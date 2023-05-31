package com.demo.controllers.model.request;

import com.demo.common.enums.AppStatus;
import com.demo.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.Size;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateEducationRequest {
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String id;

    private Long endDate;
    private Long startDate;
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String major;
    private AppStatus status;
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String universityCollege;
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String degree;
}