package com.lushu.checksystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.lushu.checksystem.dao")
public class ChecksystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChecksystemApplication.class, args);
    }

}
