package com.demo.repositories;

import com.demo.common.enums.AppStatus;
import com.demo.entities.User;
import com.demo.entities.WorkHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface WorkHistoryRepository  extends JpaRepository<WorkHistory, String>, JpaSpecificationExecutor<WorkHistory> {
    List<WorkHistory> findAllByUserIdAndStatus(String id, AppStatus active);

    List<WorkHistory> findAllByUserIdInAndStatus(List<String> userIds, AppStatus active);
}
