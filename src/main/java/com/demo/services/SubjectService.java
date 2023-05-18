package com.demo.services;

import com.demo.entities.Subject;

import java.util.List;

public interface SubjectService {
    void saveAll(List<Subject> subjects);

    List<Subject> findAllByStudentId(String id);

    void deleteByStudentId(String id);

    Subject findById(String id);


    void save(Subject subject);

    List<Subject> findAllByListStudentId(List<String> ids);
}
