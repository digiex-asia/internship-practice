package com.demo.controllers.model.response;

import com.demo.common.enums.AppStatus;
import com.demo.common.utilities.Constant;
import com.demo.entities.Class;
import com.demo.entities.Student;
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
public class ClassDetailsResponse {
    private String id;

    private String name;

    private Integer maxStudent;


    private AppStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date createdDate;
    private List<Student> students;

    public ClassDetailsResponse(String id, String name, Integer maxStudent,AppStatus status,List<Student> students) {
        this.id = id;
        this.name = name;
        this.maxStudent = maxStudent;
this.status=status;
        this.students = students;
    }

}
