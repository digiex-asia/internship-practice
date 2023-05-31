package com.demo.repositories;

import com.demo.entities.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserSkillRepository extends JpaRepository<UserSkill, String>, JpaSpecificationExecutor<UserSkill> {
    List<UserSkill> findAllByUserId(String userId);

    List<UserSkill> findAllByUserIdIn(List<String> userIds);
}
