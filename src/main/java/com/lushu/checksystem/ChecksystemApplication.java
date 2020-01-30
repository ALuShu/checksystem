package com.lushu.checksystem;

import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        User tmp = userService.selectUser("root");
        if (null == tmp){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<User> users = new ArrayList<>(1);
            User user = new User();
            user.setId(1);
            user.setUsername("root");
            user.setPassword(new BCryptPasswordEncoder().encode("root"));
            user.setRealname("系统管理员");
            user.setCreateTime(dateFormat.format(new Date()));
            users.add(user);
            int res = userService.addUsers(users);
            if (res == 1) {
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
