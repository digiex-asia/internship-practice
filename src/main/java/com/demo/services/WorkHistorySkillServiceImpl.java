package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.entities.WorkHistorySkill;
import com.demo.repositories.WorkHistorySkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkHistorySkillServiceImpl implements WorkHistorySkillService{
    final private WorkHistorySkillRepository workHistorySkillRepository;
    @Override
    public void saveAll(List<WorkHistorySkill> workHistorySkills) {
        workHistorySkillRepository.saveAll(workHistorySkills);
    }

    @Override
    public List<WorkHistorySkill> findAllByWorkHistoryId(String id) {
        return workHistorySkillRepository.findAllByWorkHistoryId(id);
    }

    @Override
    public List<WorkHistorySkill> findAllByWorkHistoryIdIn(List<String> collect) {
        return workHistorySkillRepository.findAllByWorkHistoryIdIn(collect);
    }
}
