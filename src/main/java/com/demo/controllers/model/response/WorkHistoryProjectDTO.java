package com.demo.controllers.model.response;

import com.demo.common.enums.AppStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkHistoryProjectDTO {
    private String id;

    private String description;

    private Long endDate;

    private Long startDate;

    private String name;

    private AppStatus status;

    private String workHistoryId;

    private List<ProjectSkillDTO> projectSkillDTOS;

}
