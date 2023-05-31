package com.demo.controllers.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSkillDTO {
    private String id;

    private String workHistoryProjectId;

    private String skillId;
}
