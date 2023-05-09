package com.demo.controllers.helper;

import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.DateUtil;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.Validator;
import com.demo.config.security.AuthUser;
import com.demo.entities.Session;
import com.demo.entities.User;
import com.demo.services.SessionService;
import com.demo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author DigiEx Group
 */
@Component
@Slf4j
public class AuthenticateHelper {
    final SessionService sessionService;
    final UserService userService;

    public AuthenticateHelper(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }


    public AuthUser loadAuthUserFromAuthToken(String authToken) {
        Session session = sessionService.findById(authToken);
        Validator.notNull(session, RestAPIStatus.UNAUTHORIZED, "");

        // Check expired date
        if (DateUtil.convertToUTC(new Date()).getTime() >= session.getExpiryDate().getTime()) {
            throw new ApplicationException(RestAPIStatus.UNAUTHORIZED);
        }
        User user = userService.findById(session.getUserId());
        Validator.notNull(user, RestAPIStatus.UNAUTHORIZED,"");
        return new AuthUser(user);
    }
}
