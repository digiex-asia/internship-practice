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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateWorkHistoryRequest {
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String id;
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String company;

    @Size(max = 255, message = ParamError.MAX_LENGTH)
    private String description;
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String position;
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String region;
    private Long workFrom;
    private Long workTo;
    private AppStatus status;
    List<UpdateSkillHistoryRequest> skillIds;
    List<UpdateWorkHistoryProjectRequest> workHistoryProjects;
}
