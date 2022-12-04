package com.yql.springsecuritywithjwt.config;


import cn.hutool.core.util.StrUtil;
import com.yql.springsecuritywithjwt.enums.jwt.Jwt;
import com.yql.springsecuritywithjwt.mybatis.mapper.SysFunc;
import com.yql.springsecuritywithjwt.mybatis.mapper.SysRoleFunc;
import com.yql.springsecuritywithjwt.mybatis.service.SysFuncService;
import com.yql.springsecuritywithjwt.mybatis.service.SysRoleFuncService;
import com.yql.springsecuritywithjwt.security.access.vote.AllMatchRoleVoter;
import com.yql.springsecuritywithjwt.security.core.userdetails.CustomJdbcUserDetailsService;
import com.yql.springsecuritywithjwt.security.web.access.intercept.CustomFilterSecurityInterceptor;
import com.yql.springsecuritywithjwt.security.web.authentication.JwtSecurityFilter;
import com.yql.springsecuritywithjwt.security.web.authentication.UsernamePasswordCaptchaAuthenticationFilter;
import com.yql.springsecuritywithjwt.security.web.handler.UsernamePasswordAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 表单登陆security
 * 安全  = 认证 + 授权
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final static String DEFAULT_REMEMBER_ME_KEY = "default remember me key";

    @Autowired
    private SysRoleFuncService sysRoleFuncService;
    @Autowired
    private SysFuncService sysFuncService;
    @Autowired
    private CustomJdbcUserDetailsService customJdbcUserDetailsService;
    @Autowired
    private JdbcTemplate jdbcTemplate;


//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/static/**", "/css/**", "/js/**", "/plugins/**", "/images/**", "/fonts/**");
//    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/static/**", "/captcha/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .maximumSessions(1)
                                .maxSessionsPreventsLogin(false)
                )
                .formLogin()
                .loginPage("/user/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/index")
                .failureUrl("/user/loginFailed")
                .permitAll()
                .and()
                .rememberMe()
                .rememberMeServices(tokenBasedRememberMeServices())
                .key(DEFAULT_REMEMBER_ME_KEY)
                .userDetailsService(userDetailsService())
                .tokenValiditySeconds(14 * 24 * 60 * 60)
                .and()
                .logout()
                .logoutSuccessUrl("/index")
                .deleteCookies("JSESSIONID", Jwt.JWT_TOKEN_NAME.getValue())
                .and()
                .cors()
                .and()
                .csrf().disable();

        http.addFilterBefore(jwtSecurityFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customJdbcUserDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    private UserDetailsService customJdbcUserDetailsService() {
        return this.customJdbcUserDetailsService;
    }

    private JwtSecurityFilter jwtSecurityFilter() throws Exception {
        JwtSecurityFilter jwtFilterSecurityInterceptor = new JwtSecurityFilter(authenticationManager());
        jwtFilterSecurityInterceptor.setCustomJdbcUserDetailsService(this.customJdbcUserDetailsService);
        return jwtFilterSecurityInterceptor;
    }

    private UsernamePasswordCaptchaAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
        UsernamePasswordCaptchaAuthenticationFilter authenticationFilter = new UsernamePasswordCaptchaAuthenticationFilter();
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        authenticationFilter.setAuthenticationManager(authenticationManager());
        authenticationFilter.setRememberMeServices(tokenBasedRememberMeServices());
        return authenticationFilter;
    }

    private RememberMeServices tokenBasedRememberMeServices() {
        return new TokenBasedRememberMeServices(DEFAULT_REMEMBER_ME_KEY, userDetailsService());
    }


    private FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
        CustomFilterSecurityInterceptor filterSecurityInterceptor = new CustomFilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(new DefaultFilterInvocationSecurityMetadataSource(obtainRequestMap()));
        filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager());
        return filterSecurityInterceptor;
    }

    private AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<>();
        voters.add(new AllMatchRoleVoter());

        return new AffirmativeBased(voters);
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        UsernamePasswordAuthenticationSuccessHandler authenticationSuccessHandler = new UsernamePasswordAuthenticationSuccessHandler();
        authenticationSuccessHandler.setDefaultTargetUrl("/index");
        return authenticationSuccessHandler;
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        SimpleUrlAuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();
        authenticationFailureHandler.setDefaultFailureUrl("/user/loginFailed");
        return authenticationFailureHandler;
    }


    private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> obtainRequestMap() {

        List<SysRoleFunc> sysFuncRoles = this.sysRoleFuncService.list();

        if (CollectionUtils.isEmpty(sysFuncRoles)) {
            return new LinkedHashMap<>();
        }

        Map<String, Set<Integer>> urlRoleMap = new HashMap<>();

        for (SysRoleFunc sysFuncRole : sysFuncRoles) {
            SysFunc sysFunc = this.sysFuncService.getById(sysFuncRole.getFuncId());
            String url = determineAntUrl(sysFunc.getUrl());

            Set<Integer> configAttributes = urlRoleMap.get(url);

            if (configAttributes == null) {
                configAttributes = new HashSet<>();
            }

            configAttributes.add(sysFuncRole.getRoleId());
            urlRoleMap.put(url, configAttributes);
        }

        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();

        for (String url : urlRoleMap.keySet()) {
            Set<Integer> needRoles = urlRoleMap.get(url);

            // 注意此处，我们设置ConfigAttribute为 ROLE_ 前缀加上角色标识，与 CustomJdbcUserDetailsService 里面组织UserDetails设置角色标识呼应
            requestMap.put(new AntPathRequestMatcher(url), needRoles.stream().map(role -> new SecurityConfig("ROLE_" + role)).collect(Collectors.toSet()));
        }

        return requestMap;
    }

    /**
     * 去掉最后一个 <code>/</code> 后的内容，以 <code>**</code> 代替，以匹配 <code>ant</code> 风格。
     *
     * @param url 功能地址
     * @return ant风格地址
     */
    private String determineAntUrl(String url) {

        if (StrUtil.isEmpty(url)) {
            return null;
        }

        if (StrUtil.endWithIgnoreCase(url, "/")) {
            return url.substring(0, url.lastIndexOf("/")) + "/**";
        }

        return url + "/**";
    }
}
