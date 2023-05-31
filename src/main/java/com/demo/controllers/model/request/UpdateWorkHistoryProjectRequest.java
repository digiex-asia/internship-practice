package com.demo.controllers.model.request;

import com.demo.common.enums.AppStatus;
import com.demo.common.utilities.ParamError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateWorkHistoryProjectRequest {
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String description;
    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String id;
    private Long startDate;
    private Long endDate;
    private AppStatus status;

    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String name;

    List<UpdateSkillHistoryRequest> skills;
}
