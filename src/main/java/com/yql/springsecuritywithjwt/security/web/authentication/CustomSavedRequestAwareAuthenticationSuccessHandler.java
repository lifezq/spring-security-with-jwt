package com.yql.springsecuritywithjwt.security.web.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @Package com.yql.springsecuritywithjwt.security.web.authentication
 * @ClassName CustomSavedRequestAwareAuthenticationSuccessHandler
 * @Description TODO
 * @Author Ryan
 * @Date 2022/11/27
 */
public class CustomSavedRequestAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);

        this.logger.info(String.format("IP %s，用户 %s， 于 %s 成功登录系统。", request.getRemoteHost(), authentication.getName(), LocalDateTime.now()));
    }
}

