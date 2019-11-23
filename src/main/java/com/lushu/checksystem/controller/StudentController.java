package com.lushu.checksystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lushu
 * @date 19-11-13 下午1:48
 **/
@Controller
public class StudentController {

    @RequestMapping("/say")
    public String sayHello(){
        return "sayhello";
    }

    @RequestMapping("/index")
    public String stuIndex(){
        return "index";
    }
}
