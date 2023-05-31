package com.demo.repositories;

import com.demo.common.enums.AppStatus;
import com.demo.entities.UserSkill;
import com.demo.entities.WorkHistorySkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface WorkHistorySkillRepository extends JpaRepository<WorkHistorySkill, String>, JpaSpecificationExecutor<WorkHistorySkill> {
    List<WorkHistorySkill> findAllByWorkHistoryId(String id);

    List<WorkHistorySkill> findAllByWorkHistoryIdIn(List<String> collect);
}
