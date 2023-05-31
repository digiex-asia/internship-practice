package com.demo.services;

import com.demo.entities.WorkHistoryProject;
import com.demo.entities.WorkHistorySkill;

import java.util.List;

public interface WorkHistoryProjectService {
    void saveAll(List<WorkHistoryProject> workHistoryProjects);

    List<WorkHistoryProject> findAllByWorkHistoryId(String id);

    List<WorkHistoryProject> findAllByWorkHistoryIdIn(List<String> collect);


    WorkHistoryProject findById(String id);
}
