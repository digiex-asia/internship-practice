package com.demo.services;

import com.demo.entities.Session;
import com.demo.repositories.SessionRepository;
import org.springframework.stereotype.Service;

/**
 * @author DiGiEx Group
 */
@Service
public class SessionServiceImpl implements SessionService {

    final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    @Override
    public Session findById(String id) {
        return sessionRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Session session) {
        sessionRepository.delete(session);
    }

    @Override
    public Session getId(String id) {
        return sessionRepository.findById(id).orElse(null);
    }
}
