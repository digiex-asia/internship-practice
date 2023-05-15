package com.demo.controllers.model.response;

import com.demo.common.enums.AppStatus;
import com.demo.common.enums.UserRole;
import com.demo.common.utilities.Constant;
import com.demo.entities.Class;
import com.demo.entities.Student;
import com.demo.entities.User;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ClassResponse {
    private String id;

    private String name;

    private Integer maxStudent;

    private AppStatus status;
    private Integer countStudent;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date createdDate;

    public ClassResponse(Class class_) {
        this.id = class_.getId();
        this.name = class_.getName();
        this.status = class_.getStatus();
        this.maxStudent = class_.getMaxSudent();

        this.createdDate = class_.getCreatedDate();
    }

    public ClassResponse(Class class_, List<Student> students) {
        this.id = class_.getId();
        this.name = class_.getName();
        this.status = class_.getStatus();
        this.maxStudent = class_.getMaxSudent();

        this.createdDate = class_.getCreatedDate();
        this.countStudent = students.size();
    }

}
