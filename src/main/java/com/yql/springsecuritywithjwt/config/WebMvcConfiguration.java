package com.yql.springsecuritywithjwt.config;

import com.yql.springsecuritywithjwt.security.web.access.intercept.jwt.JwtHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Package com.yql.springsecuritywithjwt.config
 * @ClassName WebMvcConfiguration
 * @Description TODO
 * @Author Ryan
 * @Date 2022/11/27
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtHandlerInterceptor())
                //排除拦截路径
                .excludePathPatterns("/user/login", "/user/loginFailed", "/static/**", "/captcha/**", "/doc.html", "/doc.html/**", "/webjars/**", "/v2/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/index");
    }
}