package com.demo.config.security;

import com.demo.common.enums.UserRole;
import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.Constant;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.controllers.helper.AuthenticateHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author DigiEx Group
 */
@Aspect
@Component
@Slf4j
public class AuthorizeValidatorInterceptor {
    final AuthenticateHelper authenticateHelper;

    public AuthorizeValidatorInterceptor(AuthenticateHelper authenticateHelper) {
        this.authenticateHelper = authenticateHelper;
    }

    @Before(value = "@annotation(com.demo.config.security.AuthorizeValidator)  && @annotation(roles)")
    public void before(JoinPoint caller, AuthorizeValidator roles) {
        // Capture access token from current request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String token = request.getHeader(Constant.HEADER_TOKEN);
        // Check Get current Authenticate user from access token
        AuthUser authUser = authenticateHelper.loadAuthUserFromAuthToken(token);
        if (authUser == null)
            throw new ApplicationException(RestAPIStatus.UNAUTHORIZED);
        // Validate Role
        boolean isValid = isValidate(authUser, roles);
        if (!isValid)
            throw new ApplicationException(RestAPIStatus.FORBIDDEN);
    }

    public boolean isValidate(AuthUser authUser, AuthorizeValidator roles) {
        for (UserRole role : roles.value()) {
            if(role == authUser.getRole())
                return true;
        }

        return false;
    }
}
