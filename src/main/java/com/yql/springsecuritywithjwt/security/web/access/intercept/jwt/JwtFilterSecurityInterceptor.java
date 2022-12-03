package com.yql.springsecuritywithjwt.security.web.access.intercept.jwt;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.yql.springsecuritywithjwt.enums.jwt.Jwt;
import com.yql.springsecuritywithjwt.security.core.userdetails.CustomJdbcUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtFilterSecurityInterceptor extends OncePerRequestFilter {
    private CustomJdbcUserDetailsService customJdbcUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String authToken = this.getTokenFromRequest(request);
        if (!StrUtil.isEmpty(authToken)) {
            JWT jwtObject = JWTUtil.parseToken(authToken);

            Object username = jwtObject.getPayload("username");

            //当token中的username不为空时进行验证token是否是有效的token
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //token中username不为空，并且Context中的认证为空，进行token验证
                //TODO,从数据库得到带有密码的完整user信息
                UserDetails userDetails = this.customJdbcUserDetailsService.loadUserByUsername(username.toString());


                if (JWTUtil.verify(authToken, Jwt.JWT_SECRET_KEY.getValue().getBytes(StandardCharsets.UTF_8))) { //如username不为空，并且能够在数据库中查到
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                    //将authentication放入SecurityContextHolder中
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }

        for (Cookie cookie : cookies) {
            if (StrUtil.equals(Jwt.JWT_TOKEN_NAME.getValue(), cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }

    public void setCustomJdbcUserDetailsService(CustomJdbcUserDetailsService customJdbcUserDetailsService) {
        this.customJdbcUserDetailsService = customJdbcUserDetailsService;
    }
}
