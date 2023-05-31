package com.demo.services;

import com.demo.entities.WorkHistory;

import java.util.List;

public interface WorkHistoryService {
    void saveAll(List<WorkHistory> workHistories);

    List<WorkHistory> findAllByUserId(String id);

    List<WorkHistory> findAllByUserIdIn(List<String> userIds);
}
