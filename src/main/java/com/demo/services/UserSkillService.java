package com.demo.services;

import com.demo.entities.UserSkill;

import java.util.List;

public interface UserSkillService {
    void saveAll(List<UserSkill> userSkills);

    List<UserSkill> findAllByUserId(String id);

    List<UserSkill> findAllByUserIdIn(List<String> userIds);
}
