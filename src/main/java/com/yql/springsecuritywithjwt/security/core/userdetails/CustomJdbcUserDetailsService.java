package com.yql.springsecuritywithjwt.security.core.userdetails;

import com.yql.springsecuritywithjwt.mybatis.mapper.SysUser;
import com.yql.springsecuritywithjwt.mybatis.mapper.SysUserRole;
import com.yql.springsecuritywithjwt.mybatis.service.SysUserRoleService;
import com.yql.springsecuritywithjwt.mybatis.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Package com.yql.springsecuritywithjwt.security.core.userdetails
 * @ClassName CustomJdbcUserDetailsService
 * @Description TODO
 * @Author Ryan
 * @Date 2022/11/27
 */
@Service
public class CustomJdbcUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = this.sysUserService.getByUsername(username);

        User.UserBuilder builder = User.builder()
                .username(sysUser.getUsername())
                .password(sysUser.getPassword());

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", sysUser.getId());

        List<SysUserRole> sysUserFuncs = this.sysUserRoleService.listByMap(params);


        List<String> roles = new ArrayList<>();
        for (SysUserRole sysUserFunc : sysUserFuncs) {
            roles.add(sysUserFunc.getRoleId().toString());
        }

        builder.roles(roles.toArray(new String[]{}));

        return builder.build();
    }
}
