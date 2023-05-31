package com.demo.repositories;

import com.demo.common.enums.AppStatus;
import com.demo.entities.Education;
import com.demo.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface SkillRepository extends JpaRepository<Skill, String>, JpaSpecificationExecutor<Skill> {
//    List<String> findByNameInAndStatus(List<String> skills, AppStatus active);

    List<Skill> findAllByNameInAndStatus(List<String> skills, AppStatus active);



    List<Skill> findAllByIdInAndStatus(List<String> collect, AppStatus active);
}
