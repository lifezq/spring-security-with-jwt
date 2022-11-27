package com.yql.springsecuritywithjwt.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【sys_user】的数据库操作Mapper
 * @createDate 2022-11-27 14:10:33
 * @Entity com.yql.springsecuritywithjwt.mybatis.mapper.SysUser
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser getByUsername(String username);
}




