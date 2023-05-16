package com.demo.entities;

import com.demo.common.enums.AppStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name = "class")
public class Class extends BaseEntity implements Serializable {
    @Id
    @Column(nullable = false, updatable = false, length = 32)
    private String id;
    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "max_student")
    private Integer maxStudent;
    @Column(name = "status", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private AppStatus status;


}
