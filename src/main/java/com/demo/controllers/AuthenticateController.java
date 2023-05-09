package com.demo.controllers;

import com.demo.common.utilities.*;
import com.demo.config.security.AuthSession;
import com.demo.config.security.AuthUser;
import com.demo.controllers.helper.*;
import com.demo.entities.Session;
import com.demo.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author DigiEx Group
 */
@RestController
@RequestMapping(ApiPath.AUTHENTICATE_API)
@Slf4j
public class AuthenticateController extends AbstractBaseController {
    final UserService userService;
    final UserHelper userHelper;
    final SessionService sessionService;
    final SessionHelper sessionHelper;


    public AuthenticateController(UserService userService, UserHelper userHelper, SessionService sessionService,
                                  SessionHelper sessionHelper) {
        this.userService = userService;
        this.userHelper = userHelper;
        this.sessionService = sessionService;
        this.sessionHelper = sessionHelper;

    }


    /**
     * User Sign In API
     *
     * @return
     */




    /**
     * get AuthInfo API
     *
     * @param authUser
     * @return
     */
    @GetMapping
    @Operation(summary = "Get Auth Info")
    public ResponseEntity<RestAPIResponse> getAuthInfo(
            @Parameter(hidden = true) @AuthSession AuthUser authUser
    ) {
        // return auth user info
        return responseUtil.successResponse(authUser);
    }


    /**
     * Logout API
     *
     * @param
     * @return ttpServletRequest
     */
    @DeleteMapping
    @Operation(summary = "Logout")
    public ResponseEntity<RestAPIResponse> logout(
            HttpServletRequest httpServletRequest
    ) {
        String authToken = httpServletRequest.getHeader(Constant.HEADER_TOKEN);
        Session session = sessionService.findById(authToken);
        if (session != null) {
            sessionService.delete(session);
        }
        return responseUtil.successResponse("Logout Successfully");
    }



}

