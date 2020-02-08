package com.lushu.checksystem;

import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.FileService;
import com.lushu.checksystem.service.UserService;
import com.lushu.checksystem.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lushu
 * @date 19-11-11 下午10:08
 **/
@Slf4j
class UserTest extends ChecksystemApplicationTests{

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    private List<User> users = new ArrayList<>();
    private List<Integer> ids = new ArrayList<>();
    private List<String> studentUsername = new ArrayList<>();
    private List<String> teacherUsername = new ArrayList<>();
    private List<Integer> studentUserId = new ArrayList<>();
    private List<Integer> teacherUserId = new ArrayList<>();
    private static final Integer studentRoleId = 3;
    private static final Integer teacherRoleId = 2;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    @Test
    void addUsersTest() {
        String fileName = "D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\excel\\test.xlsx";
        String fileName1 = "D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\excel\\testA.xls";
        //String fileName = "/usr/IntelliJ IDEA/Projects/checksystem/src/main/resources/excel/test.xlsx";
        //String fileName1 = "/usr/IntelliJ IDEA/Projects/checksystem/src/main/resources/excel/testA.xls";
        ExcelUtil<User> students = new ExcelUtil<>(User.class);
        ExcelUtil<User> teachers = new ExcelUtil<>(User.class);
        try {
            List<User> studentList = students.explain(fileName);
            List<User> teacherList = teachers.explain(fileName1);
            userService.addUsersByExcel(studentList, 3);
            userService.addUsersByExcel(teacherList, 2);
        }catch (IOException | InstantiationException |InvocationTargetException|IllegalAccessException e){
            log.error("错误", e);
        }
    }


}
