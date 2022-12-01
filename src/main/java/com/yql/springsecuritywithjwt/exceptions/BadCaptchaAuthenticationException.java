package com.yql.springsecuritywithjwt.exceptions;

import org.springframework.security.core.AuthenticationException;

public class BadCaptchaAuthenticationException extends AuthenticationException {

    public BadCaptchaAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public BadCaptchaAuthenticationException(String msg) {
        super(msg);
    }
}

