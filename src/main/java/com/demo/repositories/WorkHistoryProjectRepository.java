package com.demo.repositories;

import com.demo.common.enums.AppStatus;
import com.demo.entities.WorkHistory;
import com.demo.entities.WorkHistoryProject;
import com.demo.entities.WorkHistorySkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface WorkHistoryProjectRepository  extends JpaRepository<WorkHistoryProject, String>, JpaSpecificationExecutor<WorkHistoryProject> {
    List<WorkHistoryProject> findAllByWorkHistoryIdAndStatus(String id, AppStatus active);

    List<WorkHistoryProject> findAllByWorkHistoryIdInAndStatus(List<String> collect, AppStatus active);

    WorkHistoryProject findByIdAndStatus(String id, AppStatus active);
}
