package com.lushu.checksystem.controller;

import com.lushu.checksystem.service.AdminService;
import com.lushu.checksystem.service.StudentService;
import com.lushu.checksystem.service.TeacherService;
import org.springframework.stereotype.Controller;

/**
 * @author ALuShu
 * @date 2020/1/6
 * @throws
 * @since 1.8
 * @deprecated a controller about checking identity
 */
@Controller
public class LoginCheckController {

    private StudentService studentService;
    private TeacherService teacherService;
    private AdminService adminService;

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }


}
