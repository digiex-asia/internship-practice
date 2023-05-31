package com.demo.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@ToString
@Entity
@Table(name = "project_skill")
public class ProjectSkill extends BaseEntity implements Serializable {
    @Id
    @Column(nullable = false, updatable = false, length = 32)
    private String id;
    @Column(name = "work_history_project_id", length = 32)
    private String workHistoryProjectId;
    @Column(name = "skill_id", length = 32)
    private String skillId;
}
