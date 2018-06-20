package com.sshy.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"com.sshy.api.controller", "com.sshy.api.dao", "com.sshy.api.service", "com.sshy.api.utils"})
@MapperScan(basePackages = "com.sshy.api.dao")
@EnableAutoConfiguration
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
