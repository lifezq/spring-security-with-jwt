package com.yql.springsecuritywithjwt.controller;

import com.yql.springsecuritywithjwt.mybatis.mapper.SysUser;
import com.yql.springsecuritywithjwt.mybatis.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Package com.yql.springsecuritywithjwt.controller
 * @ClassName MemberController
 * @Description TODO
 * @Author Ryan
 * @Date 2022/11/27
 */
@Api(tags = "验证码模块")
@RequestMapping("/member")
@Controller
public class MemberController {
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "获取成员列表")
    @GetMapping("/list")
    public String list(Model model) {
        List<SysUser> list = this.sysUserService.list();
        Map<String, List<SysUser>> items = new HashMap<>();
        items.put("items", list);
        model.addAllAttributes(items);
        return "/member/list";
    }
}
