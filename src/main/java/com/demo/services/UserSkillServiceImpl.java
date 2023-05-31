package com.demo.services;

import com.demo.entities.UserSkill;
import com.demo.repositories.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSkillServiceImpl implements UserSkillService{
    final  private UserSkillRepository userSkillRepository;
    @Override
    public void saveAll(List<UserSkill> userSkills) {
        userSkillRepository.saveAll(userSkills);
    }

    @Override
    public List<UserSkill> findAllByUserId(String userId) {
        return userSkillRepository.findAllByUserId(userId);
    }

    @Override
    public List<UserSkill> findAllByUserIdIn(List<String> userIds) {
        return userSkillRepository.findAllByUserIdIn(userIds);
    }
}
