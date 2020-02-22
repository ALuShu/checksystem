package com.lushu.checksystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lushu.checksystem.constant.OtherConstant;
import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.FileService;
import com.lushu.checksystem.service.UserService;
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

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * @author lushu
 * @date 19-11-13 下午1:49
 **/
@Controller
@Slf4j
@RequestMapping("/teacher")
public class TeacherController {

    @Value("${checksystem.root}")
    private String root;
    private User user = new User();
    private FileService fileService;
    private UserService userService;

    public TeacherController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    /**
     * 教师页面跳转
     */
    @RequestMapping("/index")
    public String index(Model model){
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current", user);
        root = root + "\\" + user.getUsername() + "_" + user.getRealname();
        return "/teacher/index";
    }
    @RequestMapping("/edit")
    public String inform(Model model){
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current",user);
        return "/teacher/edit";
    }
    @RequestMapping("/personal")
    public String personal(Model model){
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current",user);
        return "/teacher/personal";
    }
    @RequestMapping("/update")
    public String update(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current", user);
        return "/teacher/update";
    }


    /**
     * 修改密码（未完成）
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public Map updatePassword(@RequestParam String jsonUsers) throws JsonProcessingException {
        Map<String, Object> res = new HashMap<>(2);
        /*ObjectMapper mapper = new ObjectMapper();
        User newUser = mapper.readValue(jsonUsers, User.class);
        User oldUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();;
        int updRes = userService.updatePassword(newUser, oldUser);
        if (updRes == 0){
            res.put("code", 0);
            res.put("msg", "error");
            return res;
        }else if (updRes == -1){
            res.put("code", -1);
            res.put("msg", "old password error");
            return res;
        }else {
            res.put("code", 1);
            res.put("msg", "success");
            return res;
        }*/
        return res;
    }

    /**
     * 教师端展示文件列表
     */
    @RequestMapping(value = "/teacherList", method = RequestMethod.GET)
    @ResponseBody
    public Map list(@RequestParam(required = false) String page
            , @RequestParam(required = false) String limit
            , @RequestParam(required = false) String path) throws ServletException, JSONException {
        long star = System.currentTimeMillis();
        Map<String, Object> res = new HashMap<>();
        if (path == null){
            path = "/";
        }

        try {
            // 返回的结果集
            List<Map<String, Object>> fileItems = new ArrayList<>();

            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(root, path))) {

                for (Path pathObj : directoryStream) {
                    // 获取文件基本属性
                    BasicFileAttributes attrs = Files.readAttributes(pathObj, BasicFileAttributes.class);

                    // 封装返回JSON数据
                    Map<String, Object> fileItem = new HashMap<>();
                    fileItem.put("name", pathObj.getFileName().toString());
                    fileItem.put("date", OtherConstant.DATE_FORMAT.format(new Date(attrs.lastModifiedTime().toMillis())));
                    fileItem.put("size", attrs.size());
                    fileItem.put("type", attrs.isDirectory() ? "dir" : "file");
                    fileItems.add(fileItem);
                }
            } catch (IOException e) {
                log.error("递归文件出错:",e);
            }
            res.put("code",0);
            res.put("msg","");
            res.put("count",fileItems.size());
            res.put("data",fileItems);
            long end = System.currentTimeMillis();
            log.info("花费时间："+(end-star)+"ms");
            return res;
        } catch (Exception e) {
            log.error("递归文件出错:"+e);
            res.put("code",-1);
            res.put("msg","数据获取错误");
            res.put("count",0);
            res.put("data","");
            return res;
        }
    }

    /**
     * 教师端展示最近批改作业列表
     */
    @RequestMapping(value = "/recentWorks", method = RequestMethod.GET)
    public void recentWorks(){}




    /**
     * 教师端作业查重
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public void checkWorks(){}


    /**
     * 教师端作业文件更新
     */
    @RequestMapping(value = "/updateWork", method = RequestMethod.POST)
    public void updateWork(){}


    /**
     * 教师端文件夹管理
     */
    @RequestMapping(value = "/manageFile", method = RequestMethod.POST)
    public void manageFile(){}


    /**
     * 教师端通知发布
     */
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public void pushInform(){}

}
