package com.demo.config.security;

import java.lang.annotation.*;

/**
 * @author DigiEx Group
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface AuthSession {
    /**
     * If true then throw ApplicationException when access token header not
     * found
     *
     * @return
     */
    boolean required() default true;

}
