package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.entities.WorkHistory;
import com.demo.repositories.WorkHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkHistoryServiceImpl implements WorkHistoryService {
    final private WorkHistoryRepository workHistoryRepository;

    @Override
    public void saveAll(List<WorkHistory> workHistories) {
        workHistoryRepository.saveAll(workHistories);
    }

    @Override
    public List<WorkHistory> findAllByUserId(String id) {
        return workHistoryRepository.findAllByUserIdAndStatus(id, AppStatus.ACTIVE);
    }

    @Override
    public List<WorkHistory> findAllByUserIdIn(List<String> userIds) {
        return workHistoryRepository.findAllByUserIdInAndStatus(userIds,AppStatus.ACTIVE);
    }
}
