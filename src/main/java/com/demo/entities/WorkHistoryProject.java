package com.demo.entities;

import com.demo.common.enums.AppStatus;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@Entity
@Table(name = "work_history_project")
public class WorkHistoryProject extends BaseEntity implements Serializable {
    @Id
    @Column(nullable = false, updatable = false, length = 32)
    private String id;
    @Column(name = "description")
    private String description;
    @Column(name = "end_date")
    private Long endDate;
    @Column(name = "start_date")
    private Long startDate;
    @Column(name = "name")
    private String name;
    @Column(name = "status",nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private AppStatus status;
    @Column(name = "work_history_id",length = 32)
    private String workHistoryId;
}
