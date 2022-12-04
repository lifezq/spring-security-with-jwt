package com.yql.springsecuritywithjwt.security.web.authentication;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.yql.springsecuritywithjwt.enums.jwt.Jwt;
import com.yql.springsecuritywithjwt.security.core.userdetails.CustomJdbcUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtSecurityFilter extends BasicAuthenticationFilter {
    private CustomJdbcUserDetailsService customJdbcUserDetailsService;

    public JwtSecurityFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public JwtSecurityFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String authToken = Jwt.getTokenFromRequest(request);
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

                    if (request.getAttribute("loginUser") == null) {
                        request.setAttribute("loginUser", userDetails.getUsername());
                    }
                    
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //将authentication放入SecurityContextHolder中
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }


    public void setCustomJdbcUserDetailsService(CustomJdbcUserDetailsService customJdbcUserDetailsService) {
        this.customJdbcUserDetailsService = customJdbcUserDetailsService;
    }
}
