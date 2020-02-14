package com.lushu.checksystem;

import com.lushu.checksystem.constant.DatabaseConstant;
import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.UserService;
import com.lushu.checksystem.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/2/14
 * @throws
 * @since
 */
@Component
@Slf4j
public class initializationBean {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init(){
        List<User> root = new ArrayList<>(1);
        User user = new User();
        user.setUsername("root");
        root.add(user);
        userService.addUsersByExcel(root, DatabaseConstant.Role.ROLE_ADMIN.ordinal()+1);
        String fileName = "D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\excel\\test.xlsx";
        String fileName1 = "D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\excel\\testA.xls";
        //String fileName = "/usr/IntelliJ IDEA/Projects/checksystem/src/main/resources/excel/test.xlsx";
        //String fileName1 = "/usr/IntelliJ IDEA/Projects/checksystem/src/main/resources/excel/testA.xls";
        ExcelUtil<User> students = new ExcelUtil<>(User.class);
        ExcelUtil<User> teachers = new ExcelUtil<>(User.class);
        try {
            List<User> studentList = students.explain(fileName);
            List<User> teacherList = teachers.explain(fileName1);
            userService.addUsersByExcel(studentList, DatabaseConstant.Role.ROLE_STUDENT.ordinal()+1);
            userService.addUsersByExcel(teacherList, DatabaseConstant.Role.ROLE_TEACHER.ordinal()+1);
        }catch (IOException | InstantiationException | InvocationTargetException |IllegalAccessException e){
            log.error("初始化错误，测试用户初始化失败", e);
        }
    }
}
