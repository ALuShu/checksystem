package com.lushu.checksystem.controller;

import com.lushu.checksystem.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lushu
 * @date 19-11-13 下午1:49
 **/
@Controller
public class TeacherController {

    private TeacherService teacherService;
    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @RequestMapping("/index")
    public ModelAndView teacherIndex(ModelAndView modelAndView){

        return modelAndView;
    }

    @RequestMapping("/private")
    public ModelAndView personal(ModelAndView modelAndView){

        return modelAndView;
    }

}
