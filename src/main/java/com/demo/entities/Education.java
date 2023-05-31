package com.demo.entities;

import com.demo.common.enums.AppStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@Entity
@Table(name = "education")
public class Education  extends BaseEntity implements Serializable {
    @Id
    @Column(nullable = false, updatable = false, length = 32)
    private String id;
    @Column(name = "end_date")
    private Long endDate;
    @Column(name = "start_date")
    private Long startDate;
    @Column(name = "major", length = 255)
    private String major;
    @Column(name = "university_college", length = 255)
    private String universityCollege;
    @Column(name = "degree", length = 255)
    private String degree;
    @Column(name = "status",nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private AppStatus status;

    @Column(name = "user_id",length = 32)
    private String userId;
}
