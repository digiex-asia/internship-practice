package com.demo.controllers.model.request;

import com.demo.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateWorkHistoryProjectRequest {
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String description;
    @Min(value = 1, message = ParamError.FIELD_NAME)
    private long startDate;
    @Min(value = 2, message = ParamError.FIELD_NAME)
    private long endDate;
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    @NotBlank(message = ParamError.FIELD_NAME)
    private String name;

    @NotEmpty(message = ParamError.FIELD_NAME)
    private List<String> skillIds;
}
