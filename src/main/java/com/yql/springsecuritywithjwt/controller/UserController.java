package com.yql.springsecuritywithjwt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Package com.yql.springsecuritywithjwt.controller
 * @ClassName LoginController
 * @Description TODO
 * @Author Ryan
 * @Date 2022/11/25
 */
@Api(tags = "用户模块")
@RequestMapping("/user")
@Controller
public class UserController {

    @ApiOperation(value = "登录")
    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }

    @ApiOperation(value = "登录失败")
    @GetMapping("/loginFailed")
    public String loginFailed() {
        return "/user/loginFailed";
    }
}
