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
    private User user = new User();

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
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
     * 管理员端批量管理用户
     */
    @RequestMapping(value = "/manageUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map addUsers(@RequestParam(required = false) MultipartFile file
            ,@RequestParam(required = false) String jsonUsers
            ,@RequestParam Integer roleId) throws InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        Map<String, Object> res = new HashMap<>();
        List<User> userList = new ArrayList<>();
        if (file != null){
            ExcelUtil<User> users = new ExcelUtil<>(User.class);
            userList = users.explain(file.getOriginalFilename());
        }else {
            ObjectMapper mapper = new ObjectMapper();
            userList = mapper.readValue(jsonUsers, new TypeReference<ArrayList<User>>() {});
        }
        int addRes = userService.addUsersByExcel(userList, roleId);
        if (addRes == (userList.size() * 2)){
            res.put("code", 1);
            res.put("msg","添加成功");
        }else {
            log.info("录入数据与数据库影响条数不一致");
            res.put("code", 0);
            res.put("msg","发生错误");
        }
        return res;
    }

    @RequestMapping(value = "/deleteUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map deleteUsers(@RequestParam String jsonIds) throws JsonProcessingException {
        Map<String, Object> res = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        List<Integer> idList = mapper.readValue(jsonIds, new TypeReference<ArrayList<Integer>>() {});
        userService.deleteUsers(idList);
        return res;
    }

    @RequestMapping(value = "/updateUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map updateUsers(@RequestParam String jsonUsers) throws JsonProcessingException {
        Map<String, Object> res = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        List<User> userList = mapper.readValue(jsonUsers, new TypeReference<ArrayList<User>>() {});
        userService.updateUsers(userList);
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
