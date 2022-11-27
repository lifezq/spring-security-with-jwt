package com.yql.springsecuritywithjwt.config;


import com.yql.springsecuritywithjwt.mybatis.mapper.SysFunc;
import com.yql.springsecuritywithjwt.mybatis.mapper.SysRoleFunc;
import com.yql.springsecuritywithjwt.mybatis.service.SysFuncService;
import com.yql.springsecuritywithjwt.mybatis.service.SysRoleFuncService;
import com.yql.springsecuritywithjwt.security.access.vote.AllMatchRoleVoter;
import com.yql.springsecuritywithjwt.security.core.userdetails.CustomJdbcUserDetailsService;
import com.yql.springsecuritywithjwt.security.web.access.intercept.CustomFilterSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 表单登陆security
 * 安全  = 认证 + 授权
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private SysRoleFuncService sysRoleFuncService;
    @Autowired
    private SysFuncService sysFuncService;
    @Autowired
    private CustomJdbcUserDetailsService customJdbcUserDetailsService;

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/static/**", "/css/**", "/js/**", "/plugins/**", "/images/**", "/fonts/**");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/static/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/user/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/index")
                .failureUrl("/user/loginFailed")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/index")
                .and()
                .csrf().disable()
        ;

        http.addFilterAfter(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customJdbcUserDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    private UserDetailsService customJdbcUserDetailsService() {
        return this.customJdbcUserDetailsService;
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
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        if (StringUtils.endsWithIgnoreCase(url, "/")) {
            return url.substring(0, url.lastIndexOf("/")) + "/**";
        }

        return url + "/**";
    }
}
