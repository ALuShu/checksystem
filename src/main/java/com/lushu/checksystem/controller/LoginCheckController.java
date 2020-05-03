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
        return "login";
    }
    @RequestMapping("/forbiddenError")
    public String forbiddenError(){
        return "forbiddenError";
    }
    @RequestMapping("/serverError")
    public String serverError(){
        return "servererror";
    }
    @RequestMapping("/notFoundError")
    public String notFoundError(){
        return "notFoundError";
    }
    @RequestMapping("/unavailableError")
    public String unavailableError(){
        return "unavailableError";
    }
    @RequestMapping("/norError")
    public String norError(){
        return "error";
    }

    /**
     * 自定义登录接口
     */
    @PostMapping("/loginCheck")
    @ResponseBody
    public void loginCheck(){}

    /**
     * 通知详情的ajax跳转
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
            return "redirect:logout";
        }else {
            User user = (User)a;
            Inform inform = informService.selectInform(id);
            model.addAttribute("detail", inform);
            model.addAttribute("current", user);
            return "informDetail";
        }
    }



}
