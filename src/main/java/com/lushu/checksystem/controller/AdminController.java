package com.lushu.checksystem.controller;

import com.lushu.checksystem.service.AdminService;
import org.springframework.stereotype.Controller;


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




}
