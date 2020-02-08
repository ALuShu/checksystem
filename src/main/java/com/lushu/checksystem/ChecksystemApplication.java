package com.lushu.checksystem;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan(value = "com.lushu.checksystem.dao")
@Slf4j
public class ChecksystemApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ChecksystemApplication.class, args);
    }

    /**
     * war包支持
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ChecksystemApplication.class);
    }
}
