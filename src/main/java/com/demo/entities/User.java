package com.demo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.demo.common.enums.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "user")
public class User extends BaseEntity implements Serializable {

    @Id
    @Column(nullable = false, updatable = false, length = 32)
    private String id;

    @Column(name = "first_name", length = 255)
    private String firstName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    @Column(name = "email", length = 255)
    private String email;
    @Column(name = "phone_number", length = 255)
    private String phoneNumber;
    @Column(name = "password_hash", columnDefinition = "TEXT")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordHash;

    @Column(name = "password_salt", length = 8)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordSalt;

    @Column(name = "role", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "status",nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private AppStatus status;


}
