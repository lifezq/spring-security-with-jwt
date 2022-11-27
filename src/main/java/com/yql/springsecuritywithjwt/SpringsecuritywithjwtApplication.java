package com.yql.springsecuritywithjwt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.yql.springsecuritywithjwt.mybatis.mapper"})
@SpringBootApplication
public class SpringsecuritywithjwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringsecuritywithjwtApplication.class, args);
    }

}
