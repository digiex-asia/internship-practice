package com.demo.services;

import com.demo.entities.WorkHistorySkill;

import java.util.List;

public interface WorkHistorySkillService {
    void saveAll(List<WorkHistorySkill> workHistorySkills);

    List<WorkHistorySkill> findAllByWorkHistoryId(String id);

    List<WorkHistorySkill> findAllByWorkHistoryIdIn(List<String> collect);
}
