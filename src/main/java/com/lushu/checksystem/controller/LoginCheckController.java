package com.lushu.checksystem.controller;

import com.lushu.checksystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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


    @RequestMapping("/login")
    public String login(){
        return "/login";
    }

    @RequestMapping("/loginError")
    public String error(){
        return "/loginError";
    }
}
