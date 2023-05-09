package com.demo.services;

import com.demo.entities.Session;


/**
 * @author DigiEx Group
 */
public interface SessionService {
    Session save(Session session);

    Session findById(String id);

    void delete(Session session);

    Session getId(String id);


}
