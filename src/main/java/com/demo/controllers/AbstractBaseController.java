package com.demo.controllers;

import com.demo.common.exceptions.ApplicationException;
import com.demo.common.utilities.ApplicationConfigureValues;
import com.demo.common.utilities.ResponseUtil;
import com.demo.common.utilities.RestAPIStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author DigiEx Group
 */
public abstract class AbstractBaseController {

    @Autowired
    public ResponseUtil responseUtil;

    @Autowired
    ApplicationConfigureValues applicationConfigureValues;


    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    ApplicationConfigureValues applicationValueConfigure;

    public void validatePageSize(int page, int size) {
        if (page < 1 || size < 1) {
            throw new ApplicationException(RestAPIStatus.BAD_REQUEST);
        }

    }
}
