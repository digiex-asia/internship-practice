package com.demo.controllers.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkHistorySkillDTO {

    private String id;

    private String workHistoryId;

    private String skillId;
}
