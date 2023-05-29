package com.demo.entities;

import com.demo.common.enums.AppStatus;
import com.demo.common.enums.Gender;
import com.demo.common.utilities.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Data
@Entity
@Table(name = "student")
public class Student extends BaseEntity implements Serializable {
    @Id
    @Column(nullable = false, updatable = false, length = 32)
    private String id;

    @Column(name = "first_name", length = 45)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "bob")
    private long bob;

    @Column(name = "address")
    private String address;

    @Column(name = "gender", length = 45)
    private Gender gender;

    @Column(name = "phone_number", length = 45)
    private String phoneNumber;

    @Column(name = "status", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private AppStatus status;

    @Column(name = "class_id",length = 32)
    private String classId;

}
