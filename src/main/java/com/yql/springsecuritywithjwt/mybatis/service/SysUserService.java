package com.yql.springsecuritywithjwt.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yql.springsecuritywithjwt.mybatis.mapper.SysUser;

/**
 * @author lenovo
 * @description 针对表【sys_user】的数据库操作Service
 * @createDate 2022-11-27 14:10:33
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);
}
