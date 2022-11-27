package com.yql.springsecuritywithjwt.config;

import org.springframework.context.annotation.Configuration;
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
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/index");
    }
}