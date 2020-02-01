package com.lushu.checksystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.FileService;
import com.lushu.checksystem.service.UserService;
import com.lushu.checksystem.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author lushu
 * @date 19-11-13 下午1:49
 **/
@Controller
@Slf4j
public class AdminController {

    @Value("${checksystem.root}")
    private String root;
    private UserService userService;
    private FileService fileService;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 管理员主页跳转
     */
    @RequestMapping("/manager")
    public String index(Model model){
        model.addAttribute("current",user);
        return "/manager";
    }


    /**
     * 管理员端展示文件列表
     */
    @RequestMapping(value = "/adminList", method = RequestMethod.GET)
    @ResponseBody
    public Map list(@RequestParam(required = false) String page
            , @RequestParam(required = false) String limit
            , @RequestParam(required = false) String path) throws ServletException, JSONException {
        long star = System.currentTimeMillis();
        Map<String, Object> res = new HashMap<>();
        if (path == null) {
            path = "/";
        }

        try {
            // 返回的结果集
            List<Map<String, Object>> fileItems = new ArrayList<>();

            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(root, path))) {
                String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat dt = new SimpleDateFormat(DATE_FORMAT);
                for (Path pathObj : directoryStream) {
                    // 获取文件基本属性
                    BasicFileAttributes attrs = Files.readAttributes(pathObj, BasicFileAttributes.class);

                    // 封装返回JSON数据
                    Map<String, Object> fileItem = new HashMap<>();
                    fileItem.put("name", pathObj.getFileName().toString());
                    fileItem.put("date", dt.format(new Date(attrs.lastModifiedTime().toMillis())));
                    fileItem.put("size", attrs.size());
                    fileItem.put("type", attrs.isDirectory() ? "dir" : "file");
                    fileItems.add(fileItem);
                }
            } catch (IOException e) {
                log.error("递归文件出错:", e);
            }
            res.put("code", 0);
            res.put("msg", "");
            res.put("count", fileItems.size());
            res.put("data", fileItems);
            long end = System.currentTimeMillis();
            log.info("花费时间：" + (end - star) + "ms");
            return res;
        } catch (Exception e) {
            log.error("递归文件出错:" + e);
            res.put("code", -1);
            res.put("msg", "数据获取错误");
            res.put("count", 0);
            res.put("data", "");
            return res;
        }
    }

    /**
     * 管理员端展示用户列表
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public Map users(@RequestParam(required = false) String page
            , @RequestParam(required = false) String limit) throws ServletException, JSONException {
        Map<String, Object> memberMap = new HashMap<>();
        memberMap.put("userMap",userService.selectAllUser());
        return memberMap;
    }


    /**
     * 管理员端搜索用户（待优化）
     */
    @RequestMapping(value = "/searchUser", method = RequestMethod.POST)
    @ResponseBody
    public Map user(@RequestParam String type, @RequestParam String keyword) throws ServletException, JSONException {
        Map<String, Object> memberMap = new HashMap<>();
        memberMap.put("user",userService.selectUser(keyword));
        return memberMap;
    }



    /**
     * 管理员端展示角色列表
     */
    @RequestMapping(value = "/roles",method = RequestMethod.GET)
    @ResponseBody
    public Map roles(){
        Map<String, Object> roleMap = new HashMap<>();
        //roleMap.put("roles",userService.selectUsersByRole());
        return roleMap;
    }


    /**
     * 管理员端批量管理用户（暂时支持学生）
     */
    @RequestMapping(value = "/manageUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map addUsers(@RequestParam(required = false) MultipartFile file
            ,@RequestParam(required = false) String jsonUsers){
        Map<String, Object> res = new HashMap<>();
        if (file != null){

            ExcelUtil<User> students = new ExcelUtil<>(User.class);
            List<String> studentUsername = new ArrayList<>();
            List<Integer> studentUserId = new ArrayList<>();
            try {
                List<User> studentList = students.explain(file.getOriginalFilename());
                int addRes = studentList.size() * 2;
                for(User s : studentList){
                    studentUsername.add(s.getUsername());
                    s.setPassword(new BCryptPasswordEncoder().encode("111111"));
                    s.setCreateTime(df.format(new Date()));
                }
                int a = userService.addUsers(studentList);
                for (User s : userService.selectUsersByUsername(studentUsername)){
                    studentUserId.add(s.getId());
                }
                int b = userService.addUserRole(studentUserId, 3);
                if (addRes == (a+b)) {
                    res.put("code", 1);
                    res.put("msg", "添加成功");
                    return res;
                }else {
                    res.put("code", 0);
                    res.put("msg","添加失败");
                    return res;
                }
            } catch (IOException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                log.error("error", e);
                res.put("code", -1);
                res.put("msg","发生错误");
                return res;
            }

        }else {

            ObjectMapper mapper = new ObjectMapper();
            List<String> studentUsername = new ArrayList<>();
            List<Integer> studentUserId = new ArrayList<>();
            try {
                List<User> studentList = mapper.readValue(jsonUsers, new TypeReference<List<User>>() {});
                int addRes = studentList.size() * 2;
                for(User s : studentList){
                    studentUsername.add(s.getUsername());
                    s.setPassword(new BCryptPasswordEncoder().encode("111111"));
                    s.setCreateTime(df.format(new Date()));
                }
                int a = userService.addUsers(studentList);
                for (User s : userService.selectUsersByUsername(studentUsername)){
                    studentUserId.add(s.getId());
                }
                int b = userService.addUserRole(studentUserId, 3);
                if (addRes == (a+b)) {
                    res.put("code", 1);
                    res.put("msg", "添加成功");
                    return res;
                }else {
                    res.put("code", 0);
                    res.put("msg","添加失败");
                    return res;
                }
            } catch (JsonProcessingException e) {
                log.error("error", e);
                res.put("code", -1);
                res.put("msg","发生错误");
                return res;
            }
        }
    }

    @RequestMapping(value = "/deleteUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map deleteUsers(@RequestParam String jsonUsers){
        Map<String, Object> res = new HashMap<>();
        return res;
    }

    @RequestMapping(value = "/updateUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map updateUsers(@RequestParam String jsonUsers){
        Map<String, Object> res = new HashMap<>();
        return res;
    }


    /**
     * 管理员端管理角色（未完成）
     */
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    @ResponseBody
    public Map addRole(@RequestParam String jsonUsers){
        Map<String, Object> res = new HashMap<>();
        return res;
    }


}
