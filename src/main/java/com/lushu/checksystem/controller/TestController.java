package com.lushu.checksystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lushu
 * @date 2019/11/22 16:21
 **/
@Controller
public class TestController {

    @GetMapping("sayhello")
    public String say(Model model){
        model.addAttribute("msg","你好啊");
        return "sayhello";
    }
}
