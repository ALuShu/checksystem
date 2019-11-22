package com.lushu.checksystem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lushu
 * @date 2019/11/22 16:21
 **/
@RestController
public class TestController {

    @RequestMapping("/test")
    public String say(){
        return "test";
    }
}
