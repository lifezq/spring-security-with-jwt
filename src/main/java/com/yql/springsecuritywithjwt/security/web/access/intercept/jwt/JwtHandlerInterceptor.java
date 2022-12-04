package com.yql.springsecuritywithjwt.security.web.access.intercept.jwt;

import cn.hutool.jwt.JWTUtil;
import com.yql.springsecuritywithjwt.enums.jwt.Jwt;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

public class JwtHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = Jwt.getTokenFromRequest(request);
        try {
            //验证令牌
            JWTUtil.verify(token, Jwt.JWT_SECRET_KEY.getValue().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
