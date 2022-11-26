package com.yql.springsecuritywithjwt.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

/**
 * 表单登陆security
 * 安全  = 认证 + 授权
 */
@Configuration
public class SecurityConfiguration {


    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeRequests()
                .mvcMatchers("/user/login", "/static/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/user/login").loginProcessingUrl("/login")
                .defaultSuccessUrl("/index")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureForwardUrl("/user/loginFailed")
                .and()
                .logout()
                .and().cors().disable();


        return httpSecurity.build();
    }

    @Bean
    UserDetailsService userDetailsService() {

        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap();
        encoders.put(encodingId, new BCryptPasswordEncoder());

        PasswordEncoder bCryptPasswordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);
        User.UserBuilder users = User.builder().passwordEncoder(bCryptPasswordEncoder::encode);
        UserDetails user = users
                .username("ryan")
                .password("123456")
                .roles("emission")
                .build();
        UserDetails admin = users
                .username("admin")
                .password("123456")
                .roles("emission", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}