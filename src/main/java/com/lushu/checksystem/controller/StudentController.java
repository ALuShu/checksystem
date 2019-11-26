package com.lushu.checksystem.controller;

import com.lushu.checksystem.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lushu
 * @date 19-11-13 下午1:48
 **/
@Controller
public class StudentController {

    private StudentService studentService;
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @RequestMapping("/say")
    public ModelAndView sayHello(ModelAndView modelAndView){
        modelAndView.setViewName("/sayhello");
        modelAndView.addObject("msg","controller测试");
        return modelAndView;
    }

    @RequestMapping("/index")
    public ModelAndView studentIndex(ModelAndView modelAndView){
        modelAndView.setViewName("/index");
        return modelAndView;
    }


    @RequestMapping("/upload")
    public ModelAndView uploadJob(ModelAndView modelAndView){
        modelAndView.setViewName("/upload");
        return modelAndView;
    }

    @RequestMapping("/private")
    public ModelAndView personal(ModelAndView modelAndView){
        modelAndView.setViewName("/private");
        return modelAndView;
    }
}
