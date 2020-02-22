package com.lushu.checksystem.controller;

import com.lushu.checksystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ALuShu
 * @date 2020/1/6
 * @throws
 * @since 1.8
 * @Description a controller about checking identity
 */
@Controller
@RequestMapping("/public")
public class LoginCheckController {

    private UserService userService;

    public LoginCheckController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 公开页面跳转
     */
    @RequestMapping("/login")
    public String login(){
        return "/login";
    }
    @RequestMapping("/inform")
    public String inform(){
        return "/informDetail";
    }
    @RequestMapping("/perError")
    public String perError(){
        return "/permissionerror";
    }
    @RequestMapping("/serError")
    public String serError(){
        return "/servererror";
    }
    @RequestMapping("/norError")
    public String error(){
        return "/error";
    }
    @GetMapping("/sayhello")
    public String say(Model model){
        model.addAttribute("msg","你好啊");
        return "/sayhello";
    }


}
