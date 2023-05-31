package com.demo.services;

import com.demo.entities.Education;

import java.util.List;

public interface EducationService {
    void saveAll(List<Education> educations);

    List<Education> findAllByUserId(String id);

    List<Education> findAllByUserIdIn(List<String> userIds);
}
