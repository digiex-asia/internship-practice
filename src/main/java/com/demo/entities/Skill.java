package com.demo.entities;

import com.demo.common.enums.AppStatus;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@Entity
@Table(name = "skill")
public class Skill extends BaseEntity implements Serializable {
    @Id
    @Column(nullable = false, updatable = false, length = 32)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "icon")
    private String icon;
    @Column(name = "status",nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private AppStatus status;
    @Column(name = "is_display")
    private boolean isDisplay=true;
}
