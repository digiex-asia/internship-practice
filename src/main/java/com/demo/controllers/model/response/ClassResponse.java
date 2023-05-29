package com.demo.controllers.model.response;

import com.demo.common.enums.AppStatus;
import com.demo.common.enums.UserRole;
import com.demo.common.utilities.Constant;
import com.demo.entities.Class;
import com.demo.entities.Student;
import com.demo.entities.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassResponse {
    private String id;
    private String name;
    private Integer maxStudent;
    private AppStatus status;
    private Integer countStudent;
    private Long createdDate;

    List<Student> students;

    public ClassResponse(Class c) {
        this.id = c.getId();
        this.name = c.getName();
        this.status = c.getStatus();
        this.maxStudent = c.getMaxStudent();
        this.createdDate = c.getCreatedDate();
    }
    public ClassResponse(Class c, List<Student> students) {
        this.id = c.getId();
        this.name = c.getName();
        this.status = c.getStatus();
        this.maxStudent = c.getMaxStudent();
        this.createdDate = c.getCreatedDate();
        this.countStudent = students.size();
    }

}
