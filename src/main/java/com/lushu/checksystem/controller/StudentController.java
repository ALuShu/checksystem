package com.lushu.checksystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lushu.checksystem.constant.DatabaseConstant;
import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.PageBean;
import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.FileService;
import com.lushu.checksystem.service.InformService;
import com.lushu.checksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lushu
 * @date 19-11-13 下午1:48
 **/
@Controller
@Slf4j
@RequestMapping("/student")
public class StudentController {

    @Value("${checksystem.root}")
    private String root;
    private String current;
    private UserService userService;
    private FileService fileService;
    private InformService informService;
    private User user = new User();

    public StudentController(UserService userService, FileService fileService, InformService informService) {
        this.userService = userService;
        this.fileService = fileService;
        this.informService = informService;
    }

    /**
     * 学生页面的一些跳转
     */
    @RequestMapping("/index")
    public String index(Model model) {
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/student/index";
        }
    }
    @RequestMapping("/upload")
    public String upload(@RequestParam String requestPath, Model model) {
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            model.addAttribute("currentPath", current);
            current = requestPath;
            return "/student/upload";
        }
    }
    @RequestMapping("/personal")
    public String personal(Model model) {
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/student/personal";
        }
    }
    @RequestMapping("/update")
    public String update(Model model) {
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/student/update";
        }
    }
    @RequestMapping("/search")
    public String teachers(Model model) {
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/student/teachers";
        }
    }
    @RequestMapping("/search/{username}")
    public String teacher(Model model, @PathVariable String username) {
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            User searchRes = (User) userService.selectUser(username).get("user");
            model.addAttribute("current", user);
            model.addAttribute("res", searchRes);
            return "/student/teachers";
        }
    }


    /**
     * 通知列表，可筛选
     */
    @RequestMapping(value = "/showInforms", method = RequestMethod.GET)
    @ResponseBody
    public Map showInforms(@RequestParam(value = "type", required = false) int type
            , @RequestParam(value = "department", required = false) String department
            , @RequestParam(value = "page") int page
            , @RequestParam(value = "limit") int limit) {
        HashMap<String, Object> informMap = new HashMap<>(4);
        PageBean<Inform> pageBean = informService.selectInformsBySort(type, page, limit, department);
        informMap.put("data", pageBean.getList());
        informMap.put("msg", "");
        informMap.put("code", 0);
        informMap.put("count", pageBean.getTotalRecord());
        return informMap;
    }

    /**
     * 修改密码（后续优化：将旧密码的确认做成异步）
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public Map updatePassword(@RequestBody Map jsonUsers) throws JsonProcessingException {
        Map<String, Object> res = new HashMap<>(2);
        String oldPassword = (String) jsonUsers.get("oldPassword");
        String newPassword = (String) jsonUsers.get("newPassword");
        User oldUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int updRes = userService.updatePassword(newPassword, oldPassword, oldUser);
        if (updRes == 0){
            res.put("code", 0);
            res.put("msg", "error");
        }else if (updRes == -1){
            res.put("code", -1);
            res.put("msg", "old password error");
        }else {
            res.put("code", 1);
            res.put("msg", "success");
        }
        return res;
    }

    /**
     * 学生端显示文件列表
     */
    @RequestMapping(value = "/studentList", method = RequestMethod.GET)
    @ResponseBody
    public Map list(@RequestParam int page
            , @RequestParam int limit
            , @RequestParam(required = false) String path) throws ServletException, JSONException {
        Map<String, Object> res = new HashMap<>();
        if ((current != null && !"".equals(current) && path == null) || "/".equals(path)){
            path = current;
        }else if (current == null || "".equals(current)){
            path = "/test";
        }
        PageBean<Map<String, Object>> fileList = fileService.showFileList(path, page, limit);
        if (fileList == null){
            res.put("data",null);
            res.put("code",0);
            res.put("msg","");
            res.put("count",0);
        }else {
            res.put("data", fileList.getList());
            res.put("code", 0);
            res.put("msg", "");
            res.put("count", fileList.getTotalRecord());
        }
        return res;
    }


    /**
     * 学生端上传文件方法（后续优化：多文件上传时不像现在的要请求多次此方法）
     */
    @PostMapping("/uploadFile")
    @ResponseBody
    public Map upload(HttpServletRequest request, @RequestParam("path") String path) {
        Map<String, Object> json = new HashMap<>();
        if (current != null && !"".equals(current) || "/".equals(path)){
            path = current;
        }else if (current == null || "".equals(current)){
            path = "/test";
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = user.getId();
        if (fileMap == null || fileMap.size() == 0) {
            json.put("code", 0);
            json.put("msg", "请选择作业文件!");
            return json;
        }
        Collection<MultipartFile> files = fileMap.values();
        int res = fileService.addFiles(files, path, id);
        if (res == fileMap.size()){
            json.put("code", 1);
            json.put("msg", "上传成功！");
            return json;
        }else if (res == 0){
            json.put("code", 2);
            json.put("msg", "覆盖操作！");
            return json;
        }else {
            json.put("code", -1);
            json.put("msg", "上传失败！");
            return json;
        }
    }


    /**
     * 学生端搜索教师
     */
    @RequestMapping(value = "/searchTeacher", method = RequestMethod.GET)
    @ResponseBody
    public Map searchTeacher(String username) {
        HashMap<String, Object> des = new HashMap<>();
        des.put("code",1);
        des.put("msg","转发");
        des.put("username",username);
        return des;
    }

    /**
     * 教师列表
     */
    @RequestMapping(value = "/showTeachers", method = RequestMethod.GET)
    @ResponseBody
    public Map showTeachers(@RequestParam int page, @RequestParam int limit) {
        Map<String, Object> map = new HashMap<>();
        PageBean<User> res = userService.selectUsersByRole(page, limit, DatabaseConstant.Role.ROLE_TEACHER.ordinal() + 1);
        map.put("data", res.getList());
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", res.getTotalRecord());
        return map;
    }

    /**
     * 以往作业列表（未完成）
     */
    @RequestMapping(value = "/showOldWorks", method = RequestMethod.GET)
    public void showOldWorks() {
    }


}
