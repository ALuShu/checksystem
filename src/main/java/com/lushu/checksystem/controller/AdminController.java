package com.lushu.checksystem.controller;

import com.lushu.checksystem.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author lushu
 * @date 19-11-13 下午1:49
 **/
@Controller
public class AdminController {


    private AdminService adminService;
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping("/manager")
    public ModelAndView supervise(ModelAndView modelAndView){
        modelAndView.setViewName("/manage");
        return modelAndView;
    }


}
