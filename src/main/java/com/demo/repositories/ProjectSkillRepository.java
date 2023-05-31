package com.demo.repositories;

import com.demo.common.enums.AppStatus;
import com.demo.entities.Education;
import com.demo.entities.ProjectSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, String>, JpaSpecificationExecutor<ProjectSkill> {
    List<ProjectSkill> findAllByWorkHistoryProjectId(String id);

    List<ProjectSkill> findAllByWorkHistoryProjectIdIn(List<String> collect);
}
