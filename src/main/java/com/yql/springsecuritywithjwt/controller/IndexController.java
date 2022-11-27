package com.yql.springsecuritywithjwt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
@RequestMapping("/")
@Controller
public class IndexController {

    @GetMapping("/index")
    public String index(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("身份信息：" + authentication.getPrincipal());
        System.out.println("权限信息：" + authentication.getAuthorities());
        Object grantedAuthorities = authentication.getPrincipal();

        User user = null;
        if (grantedAuthorities instanceof User) {
            user = (User) grantedAuthorities;
        }
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null && user != null) {
            session.setAttribute("loginUser", user.getUsername());
        }

        return "/index";
    }
}
