package com.yql.springsecuritywithjwt.security.web.handler;

import cn.hutool.jwt.JWTUtil;
import com.yql.springsecuritywithjwt.enums.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UsernamePasswordAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private static final int JWT_MAX_AGE = 60 * 60 * 24 * 14;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        User user = (User) authentication.getPrincipal();
        Assert.notNull(user, "authentication failure, can not get principal");


        Map<String, Object> payloads = new HashMap<>();
        payloads.put("username", user.getUsername());

        String jwtToken = JWTUtil.createToken(payloads, Jwt.JWT_SECRET_KEY.getValue().getBytes(StandardCharsets.UTF_8));
        this.setCookie(jwtToken, request, response);

        request.getSession(false).setAttribute("loginUser", user.getUsername());

        super.onAuthenticationSuccess(request, response, authentication);
    }

    protected void setCookie(String token, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(Jwt.JWT_TOKEN_NAME.getValue(), token);
        cookie.setMaxAge(JWT_MAX_AGE);
        cookie.setPath(this.getCookiePath(request));


        cookie.setSecure(request.isSecure());
        cookie.setHttpOnly(false);
        response.addCookie(cookie);
    }

    private String getCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }
}
