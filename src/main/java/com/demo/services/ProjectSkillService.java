package com.demo.services;

import com.demo.entities.ProjectSkill;

import java.util.List;

public interface ProjectSkillService {
    void saveAll(List<ProjectSkill> projectSkills);

    List<ProjectSkill> findAllByWorkHistoryProjectId(String id);

    List<ProjectSkill> findAllByWorkHistoryProjectIdIn(List<String> collect);
}
