package com.demo.repositories;

import com.demo.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DigiEx Group
 */
@Repository
@Transactional
public interface SessionRepository extends JpaRepository<Session, String> {


    @Modifying
    @Query(value = "DELETE FROM Session WHERE userId = :userId")
    void deleteSessionsByUserId(@Param(value = "userId") String userId);
}
