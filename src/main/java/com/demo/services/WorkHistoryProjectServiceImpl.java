package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.entities.WorkHistoryProject;
import com.demo.entities.WorkHistorySkill;
import com.demo.repositories.WorkHistoryProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkHistoryProjectServiceImpl implements WorkHistoryProjectService {
    final private WorkHistoryProjectRepository workHistoryProjectRepository;

    @Override
    public void saveAll(List<WorkHistoryProject> workHistoryProjects) {
        workHistoryProjectRepository.saveAll(workHistoryProjects);
    }

    @Override
    public List<WorkHistoryProject> findAllByWorkHistoryId(String id) {
        return workHistoryProjectRepository.findAllByWorkHistoryIdAndStatus(id, AppStatus.ACTIVE);
    }

    @Override
    public List<WorkHistoryProject> findAllByWorkHistoryIdIn(List<String> collect) {
        return workHistoryProjectRepository.findAllByWorkHistoryIdInAndStatus(collect,AppStatus.ACTIVE);
    }

    @Override
    public WorkHistoryProject findById(String id) {
        return workHistoryProjectRepository.findByIdAndStatus(id,AppStatus.ACTIVE);
    }
}
