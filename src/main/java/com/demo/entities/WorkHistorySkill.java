package com.demo.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@Entity
@Table(name = "work_history_skill")
public class WorkHistorySkill {

    @Id
    @Column(nullable = false, updatable = false, length = 32)
    private String id;

    @Column(name = "work_history_id", length = 32)
    private String workHistoryId;
    @Column(name = "skill_id", length = 32)
    private String skillId;


}
