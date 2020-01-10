package com.lushu.checksystem.controller;

import com.lushu.checksystem.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lushu
 * @date 19-11-13 下午1:49
 **/
@Controller
public class TeacherController {

    private FileService fileService;
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping("/teacher")
    public ModelAndView teacherIndex(ModelAndView modelAndView){
        modelAndView.setViewName("/teacherindex");
        return modelAndView;
    }

    @RequestMapping("/private")
    public ModelAndView personal(ModelAndView modelAndView){
        modelAndView.setViewName("/teacherprivate");
        return modelAndView;
    }

}
