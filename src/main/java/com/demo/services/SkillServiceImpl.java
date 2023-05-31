package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.entities.Skill;
import com.demo.repositories.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    final private SkillRepository skillRepository;
    @Override
    public List<Skill> findAllByNameIn(List<String> skills) {
        return skillRepository.findAllByNameInAndStatus(skills, AppStatus.ACTIVE);
    }

    @Override
    public List<Skill> findAllByIdIn(List<String> collect) {
        return skillRepository.findAllByIdInAndStatus(collect,AppStatus.ACTIVE);
    }
}
