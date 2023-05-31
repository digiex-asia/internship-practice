package com.demo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@ToString
@Entity
@Table(name = "user_skill")
public class UserSkill {
    @Id
    @Column(nullable = false, updatable = false, length = 32)
    private String id;

    @Column(name = "user_id",length = 32)
    private String userId;
    @Column(name = "skill_id",length = 32)
    private String skillId;
    @Column(name = "experience")
    private int experience;
}
