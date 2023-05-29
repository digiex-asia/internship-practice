package com.demo.entities;

import com.demo.common.enums.AppStatus;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "subject")
public class Subject extends BaseEntity implements Serializable {
    @Id
    @Column(nullable = false, updatable = false, length = 32)
    private String id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "score", nullable = false, length = 45)
    private Double score;

    @Column(name = "number_of_lessons", length = 45)
    private Integer numberOfLessons;

    @Column(name = "status", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private AppStatus status;

    @Column(name = "student_id",length = 32)
    private String studentId;
}
