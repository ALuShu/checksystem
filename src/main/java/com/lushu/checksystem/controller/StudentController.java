package com.lushu.checksystem.controller;

import com.lushu.checksystem.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

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

    @RequestMapping("/student")
    public ModelAndView studentIndex(ModelAndView modelAndView){
        modelAndView.setViewName("/index");
        return modelAndView;
    }


    @GetMapping("/upload")
    public String uploadJob(){
        return "/upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String uploadres(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        String fileName = file.getOriginalFilename();
        String filePath = request.getServletContext().getRealPath("upload");
        File dest = new File(filePath, UUID.randomUUID().toString()+fileName);
        try {
            file.transferTo(dest);
            return "上传成功";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败！";
    }



    @RequestMapping("/personal")
    public ModelAndView personal(ModelAndView modelAndView){
        modelAndView.setViewName("/private");
        return modelAndView;
    }
}
