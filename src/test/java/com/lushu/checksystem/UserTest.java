package com.lushu.checksystem;

import com.lushu.checksystem.pojo.Authority;
import com.lushu.checksystem.pojo.Role;
import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.FileService;
import com.lushu.checksystem.service.UserService;
import com.lushu.checksystem.util.ExcelUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lushu
 * @date 19-11-11 下午10:08
 **/
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
    void selectAllUserTest(){
        users = userService.selectAllUser();
        System.out.println(users);
    }

    @Test
    void selectUserTest(){
        User user = userService.selectUser("张三");
        if (user == null){
            System.out.println("查无此人");
        }
        System.out.println(user);
    }

    @Test
    void selectRoleByUsernameTest(){
        Role role = userService.selectRoleByUsername("系统管理员");
        if (role == null){
            System.out.println("查无此人");
        }
        System.out.println(role);
    }

    @Test
    void selectAuthoritiesByUsernameTest(){
        List<Authority> authorities = userService.selectAuthoritiesByUsername("系统管理员");
        if (authorities.size() < 1){
            System.out.println("查无此人");
        }
        for (Authority authority: authorities){
            System.out.println(authority.getTag());
        }
    }

    @Test
    void selectUserByRoleTest(){
        users = userService.selectUsersByRole(1);
        for (User user : users){
            System.out.println(user);
        }
    }

    @Test
    void addUsersTest(){
        User user = new User();
        user.setUsername("root");
        user.setRealname("系统管理员");
        user.setCreateTime(df.format(new Date()));
        users.add(user);
        userService.addUsers(users);
    }

    @Test
    void addUsersTest2() {
        String fileName = "D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\excel\\test.xlsx";
        String fileName1 = "D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\excel\\testA.xls";
        //String fileName = "/usr/IntelliJ IDEA/Projects/checksystem/src/main/resources/excel/test.xlsx";
        //String fileName1 = "/usr/IntelliJ IDEA/Projects/checksystem/src/main/resources/excel/testA.xls";
        ExcelUtil<User> students = new ExcelUtil<>(User.class);
        ExcelUtil<User> teachers = new ExcelUtil<>(User.class);
        try {
            List<User> studentList = students.explain(fileName);
            List<User> teacherList = teachers.explain(fileName1);
            for(User s : studentList){
                studentUsername.add(s.getUsername());
                s.setPassword(new BCryptPasswordEncoder().encode("111111"));
                s.setCreateTime(df.format(new Date()));
                System.out.println(s.toString());
            }
            for(User s : teacherList){
                teacherUsername.add(s.getUsername());
                s.setPassword(new BCryptPasswordEncoder().encode("111111"));
                s.setCreateTime(df.format(new Date()));
                System.out.println(s.toString());
            }
            userService.addUsers(studentList);
            userService.addUsers(teacherList);
            for (User s : userService.selectUsersByUsername(studentUsername)){
                studentUserId.add(s.getId());
            }
            for (User s : userService.selectUsersByUsername(teacherUsername)){
                fileService.newTeacherFile(s);
                teacherUserId.add(s.getId());
            }
            userService.addUserRole(studentUserId, studentRoleId);
            userService.addUserRole(teacherUserId, teacherRoleId);
        } catch (IOException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteUsersTest(){
        boolean flag = true;
        if (flag) {
            ids.add(2);
            ids.add(4);
            ids.add(6);
            ids.add(8);
            ids.add(9);
            int res = userService.deleteUsers(ids);
            System.out.println((res==ids.size())?"删除成功，影响"+res+"条数据" : "删除失败");
        }else {
            List<User> users = userService.selectAllUser();
            for (User user : users){
                ids.add(user.getId());
            }
            int res = userService.deleteUsers(ids);
            System.out.println((res==ids.size())?"删除成功，影响"+res+"条数据" : "删除失败");
        }
    }

    @Test
    void updateUsersTest(){
        User user1 = userService.selectById(2);
        User user2 = userService.selectById(3);
        User user3 = userService.selectById(4);
        User user4 = userService.selectById(5);
        User user5 = userService.selectById(6);
        User user6 = userService.selectById(7);
        User user7 = userService.selectById(8);
        User user8 = userService.selectById(9);
        User user9 = userService.selectById(10);
        User user10 = userService.selectById(11);
        user1.setUsername("10001");
        user2.setPassword("123456");
        user3.setRealname("测试人员");
        user4.setDepartment("数码媒体");
        user5.setMajor("大数据工程");
        user6.setLastLoginTime(df.format(new Date()));
        user7.setAccountNonExpired(false);
        user8.setAccountNonLocked(false);
        user9.setCredentialsNonExpired(false);
        user10.setEnabled(false);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
        users.add(user7);
        users.add(user8);
        users.add(user9);
        users.add(user10);
        int res = userService.updateUsers(users);
        System.out.println((res==users.size())?"更新成功，影响"+res+"条数据" : "更新失败");
    }

    @Test
    void updatePasswordTest(){
        User user = new User();
        user.setId(1);
        user.setPassword("root");
        userService.updatePassword(user);
    }


}
