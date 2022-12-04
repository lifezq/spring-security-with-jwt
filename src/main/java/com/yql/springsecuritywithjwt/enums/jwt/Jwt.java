package com.yql.springsecuritywithjwt.enums.jwt;

import cn.hutool.core.util.StrUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public enum Jwt {
    JWT_TOKEN_NAME("JwtAuthorization", "token name"),
    JWT_SECRET_KEY("my_jwt_secret_key", "my jwt secret key");

    private String value;
    private String desc;

    Jwt(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }

        for (Cookie cookie : cookies) {
            if (StrUtil.equals(JWT_TOKEN_NAME.getValue(), cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
