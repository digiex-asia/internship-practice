package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.controllers.model.response.StudentResponse;
import com.demo.entities.Student;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {

    Student getByEmail(String email);

    Student getByPhoneNumber(String phoneNumber);

    void save(Student newStudent);

    Student findById(String id);

    List<Student> findAllByClassId(String id);

    Page<StudentResponse> getPageMember(String email,String phone,String ClassId,String searchKey, String sortField, boolean ascSort, int pageNumber, int pageSize);

    void saveAll(List<Student> students);

    List<Student> findAllByStatus(AppStatus active);
}
