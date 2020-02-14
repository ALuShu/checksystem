package com.lushu.checksystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 公开页面跳转
     */
    @RequestMapping("/login")
    public String login(){
        return "/login";
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

    /**
     * 修改密码
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public Map updatePassword(@RequestParam String jsonUsers) throws JsonProcessingException {
        Map<String, Object> res = new HashMap<>(3);
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(jsonUsers, User.class);
        int updateRes = userService.updatePassword(user);
        res.put("code", 1);
        res.put("msg", "影响记录数："+updateRes+"条");
        return res;
    }
}
