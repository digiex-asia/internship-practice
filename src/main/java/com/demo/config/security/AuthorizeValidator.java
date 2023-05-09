package com.demo.config.security;


import com.demo.common.enums.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author DigiEx Group
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizeValidator {
    UserRole[] value() default UserRole.ADMIN;
}
