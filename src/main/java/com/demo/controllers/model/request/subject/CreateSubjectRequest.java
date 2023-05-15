package com.demo.controllers.model.request.subject;

import com.demo.common.enums.AppStatus;
import com.demo.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSubjectRequest {
    @Size(max = 45, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String name;
    @Size(max = 10, message = ParamError.MAX_VALUE)
    @Size(max = 0, message = ParamError.MIN_VALUE)
    @NotNull(message = ParamError.FIELD_NAME)
    private Double score;

    @NotNull(message = ParamError.FIELD_NAME)

    private Integer numberOfLessons;

    @NotNull(message = ParamError.FIELD_NAME)
    private AppStatus status;

}
