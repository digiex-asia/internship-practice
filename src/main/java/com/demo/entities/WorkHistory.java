package com.demo.entities;

import com.demo.common.enums.AppStatus;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@Entity
@Table(name = "work_history")
public class WorkHistory extends BaseEntity implements Serializable {
    @Id
    @Column(nullable = false, updatable = false, length = 32)
    private String id;

    @Column(name = "company")
    private String company;
    @Column(name = "description")
    private String description;
    @Column(name = "region")
    private String region;
    @Column(name = "position")
    private String position;
    @Column(name = "work_from")
    private Long workFrom;
    @Column(name = "work_to")
    private Long workTo;
    @Column(name = "status",nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private AppStatus status;
    @Column(name = "user_id",length = 32)
    private String userId;
}
