package com.lushu.checksystem.controller;

import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.InformService;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private InformService informService;

    public LoginCheckController(InformService informService) {
        this.informService = informService;
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
     * 下面两个方法服务于ajax跳转
     */
    @RequestMapping(value = "/inform", method = RequestMethod.GET)
    @ResponseBody
    public Map inform(Inform detailJson){
        HashMap<String,Object> resMap = new HashMap<>();
        resMap.put("code",1);
        resMap.put("id",detailJson.getId());
        return resMap;
    }
    @RequestMapping(value = "/informDetail/{id}",method = RequestMethod.GET)
    public String informDetail(Model model, @PathVariable("id") Integer id){
        //超时会变成匿名用户anonymousUser，重定向到注销状态
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            User user = (User)a;
            Inform inform = informService.selectInform(id);
            model.addAttribute("detail", inform);
            model.addAttribute("current", user);
            return "/informDetail";
        }
    }



}
