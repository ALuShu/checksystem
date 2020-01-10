package com.lushu.checksystem.controller;

import com.lushu.checksystem.service.UserService;
import org.springframework.stereotype.Controller;

/**
 * @author ALuShu
 * @date 2020/1/6
 * @throws
 * @since 1.8
 * @Description a controller about checking identity
 */
@Controller
public class LoginCheckController {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
