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

    /**
     * 登录跳转
     */
    @RequestMapping("/login")
    public String login(){
        return "/login";
    }

    /**
     * 登陆失败/权限不足 跳转
     */
    @RequestMapping("/loginError")
    public String error(){
        return "/loginError";
    }

    /**
     * 修改密码跳转
     */


    /**
     * 修改密码
     */
}
