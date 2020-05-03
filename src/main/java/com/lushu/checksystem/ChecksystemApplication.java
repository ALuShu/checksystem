package com.lushu.checksystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan(value = "com.lushu.checksystem.dao")
public class ChecksystemApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ChecksystemApplication.class, args);
    }

}
