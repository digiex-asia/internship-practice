package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.entities.ProjectSkill;
import com.demo.repositories.ProjectSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectSkillServiceImpl implements ProjectSkillService{
    final  private ProjectSkillRepository projectSkillRepository;
    @Override
    public void saveAll(List<ProjectSkill> projectSkills) {
     projectSkillRepository.saveAll(projectSkills);
    }

    @Override
    public List<ProjectSkill> findAllByWorkHistoryProjectId(String id) {
        return projectSkillRepository.findAllByWorkHistoryProjectId(id);
    }

    @Override
    public List<ProjectSkill> findAllByWorkHistoryProjectIdIn(List<String> collect) {
        return projectSkillRepository.findAllByWorkHistoryProjectIdIn(collect);
    }
}
