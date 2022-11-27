package com.yql.springsecuritywithjwt.mybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yql.springsecuritywithjwt.mybatis.mapper.SysUser;
import com.yql.springsecuritywithjwt.mybatis.mapper.SysUserMapper;
import com.yql.springsecuritywithjwt.mybatis.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * @author lenovo
 * @description 针对表【sys_user】的数据库操作Service实现
 * @createDate 2022-11-27 14:10:33
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    @Override
    public SysUser getByUsername(String username) {
        return this.getBaseMapper().getByUsername(username);
    }
}




