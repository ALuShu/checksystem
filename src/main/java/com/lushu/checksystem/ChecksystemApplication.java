package com.lushu.checksystem;

import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@MapperScan(value = "com.lushu.checksystem.dao")
@Slf4j
public class ChecksystemApplication {

    private static UserService userService;
    public ChecksystemApplication(UserService userService) {
        ChecksystemApplication.userService = userService;
    }

    public static void init() {
        if (userService.countUsers() == 0){
            List<User> users = new ArrayList<>(1);
            User user = new User();
            users.add(user);
            int res = userService.addUsersByExcel(users, 1);
            if (res == 2) {
                log.info("初始化成功");
            } else {
                log.info("初始化失败");
            }
        }else {
            log.info("初始化成功");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ChecksystemApplication.class, args);
        ChecksystemApplication.init();
    }

}
