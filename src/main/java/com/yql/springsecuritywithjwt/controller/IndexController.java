package com.yql.springsecuritywithjwt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @Package com.yql.springsecuritywithjwt.controller
 * @ClassName IndexController
 * @Description TODO
 * @Author Ryan
 * @Date 2022/11/26
 */
@Api(tags = "首页模块")
@RequestMapping("/")
@Controller
public class IndexController {

    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(HttpSession session) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("身份信息：" + authentication.getPrincipal());
//        System.out.println("权限信息：" + authentication.getAuthorities());

        return "/index";
    }
}
