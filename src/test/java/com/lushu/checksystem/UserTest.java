package com.lushu.checksystem;

import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.UserService;
import com.lushu.checksystem.util.ExcelUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author lushu
 * @date 19-11-11 下午10:08
 **/
class UserTest extends ChecksystemApplicationTests{

    @Autowired
    private UserService userService;
    private List<User> users = new ArrayList<>();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    @Test
    void selectAllUserTest(){}

    @Test
    void selectUserTest(){}

    @Test
    void selectRoleByUsernameTest(){}

    @Test
    void selectAuthoritiesByUsernameTest(){}

    @Test
    void addUsersTest(){
        User user = new User();
        user.setUsername("1640706001");
        user.setRealname("测试人员");
        user.setDepartment("计算机系");
        user.setMajor("计算机科学与技术");
        user.setCreateTime(df.format(new Date()));
        users.add(user);
        userService.addUsers(users);
    }

    @Test
    void addUsersTest2() {
        String fileName = "D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\excel\\test.xlsx";
        String fileName1 = "D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\excel\\testA.xls";
        ExcelUtil<User> students = new ExcelUtil<>(User.class);
        ExcelUtil<User> teachers = new ExcelUtil<>(User.class);
        try {
            List<User> studentList = students.explain(fileName);
            List<User> teacherList = teachers.explain(fileName1);
            for(User s : studentList){
                s.setCreateTime(df.format(new Date()));
                System.out.println(s.toString());
            }
            for(User s : teacherList){
                s.setCreateTime(df.format(new Date()));
                System.out.println(s.toString());
            }
            assertEquals(studentList.size(), userService.addUsers(studentList));
            assertEquals(teacherList.size(), userService.addUsers(teacherList));
        } catch (IOException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteUsersTest(){}

    @Test
    void updateUsersTest(){}

    @Test
    void updatePasswordTest(){}


}
