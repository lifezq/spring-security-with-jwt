package com.yql.springsecuritywithjwt.controller;

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
@RequestMapping("/user")
@Controller
public class UserController {

    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }

    @GetMapping("/loginFailed")
    public String loginFailed() {
        return "/user/loginFailed";
    }
}
