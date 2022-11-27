package com.yql.springsecuritywithjwt.security.web.authentication.logout;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @Package com.yql.springsecuritywithjwt.security.web.authentication.logout
 * @ClassName CustomLogoutSuccessHandler
 * @Description TODO
 * @Author Ryan
 * @Date 2022/11/27
 */
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onLogoutSuccess(request, response, authentication);

        this.logger.info(String.format("IP %s，用户 %s， 于 %s 退出系统。", request.getRemoteHost(), authentication.getName(), LocalDateTime.now()));

        try {
            // 发邮件
            System.out.println("发送邮件...");

            // 发短信
            System.out.println("发送短信...");

            // 发微信
            System.out.println("发送微信...");
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
        }
    }

}
