package com.demo.controllers.model.response;

import com.demo.common.enums.AppStatus;
import com.demo.entities.WorkHistoryProject;
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
public class WorkHistoryDTO {
    private String id;

    private String company;
    private String description;
    private String region;
    private String position;

    private Long workFrom;

    private Long workTo;

    private AppStatus status;

    private String userId;

    private List<WorkHistoryProjectDTO> workHistoryProjectDTOList;
    private List<WorkHistorySkillDTO> workHistorySkills;

}
