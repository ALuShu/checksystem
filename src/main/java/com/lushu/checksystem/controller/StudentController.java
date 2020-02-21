package com.lushu.checksystem.controller;

import com.lushu.checksystem.constant.BasicConstant;
import com.lushu.checksystem.constant.DatabaseConstant;
import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.PageBean;
import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.FileService;
import com.lushu.checksystem.service.InformService;
import com.lushu.checksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
     * 学生主页跳转
     */
    @RequestMapping("/index")
    public String index(Model model) {
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current", user);
        return "/student/index";
    }

    @RequestMapping("/upload")
    public String upload(String path, Model model) {
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current", user);
        current = root + "\\" + path;
        return "/student/upload";
    }

    @RequestMapping("/personal")
    public String personal(Model model) {
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current", user);
        return "/student/personal";
    }

    @RequestMapping("/update")
    public String update(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current", user);
        return "/update";
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
     * 以往作业列表（未完成）
     */
    @RequestMapping(value = "/showOldWorks", method = RequestMethod.GET)
    public void showOldWorks() {
    }

    /**
     * 教师列表（未完成）
     */
    @RequestMapping(value = "/showTeachers", method = RequestMethod.GET)
    @ResponseBody
    public Map showTeachers(@RequestParam int page, @RequestParam int limit) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("teachers", userService.selectUsersByRole(page, limit, DatabaseConstant.Role.ROLE_TEACHER.ordinal() + 1));
        map.put("code", 1);
        map.put("msg", "查询成功");
        return map;
    }

    /**
     * 学生端显示文件列表（未完成）
     */


    /**
     * 学生端上传文件方法（未完成）
     */
    @PostMapping("/uploadFile")
    @ResponseBody
    public Map upload(HttpServletRequest request, @RequestParam("path") String path) {
        Map<String, Object> json = new HashMap<>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        if (fileMap == null || fileMap.size() == 0) {
        }
        Collection<MultipartFile> files = fileMap.values();

        //com.lushu.checksystem.pojo.File daoDest = new com.lushu.checksystem.pojo.File();
        /*if (file.isEmpty()) {
            json.put("code", 1);
            json.put("msg", "请选择作业文件!");
            json.put("data", "{'file':'" + file.getOriginalFilename() + "'}");
            return json;
        }
        File rootPath = new File("src/main/resources/root" + path);*/
        /*if (!rootPath.exists()){
            rootPath.mkdir();
        }*/
        //String fileName = file.getOriginalFilename();
        //String filePath = rootPath.getAbsolutePath();
        /*daoDest.setName(fileName);
        daoDest.setPath(filePath);
        daoDest.setSize(file.getSize());*/
        //File dest = new File(filePath, fileName);
        /*try {
            if (!dest.exists()) {
                //fileService.addFiles(daoDest);
                file.transferTo(dest);
                json.put("code", 0);
                json.put("msg", "上传成功！");
                json.put("data", "{'file':'" + file.getOriginalFilename() + "'}");
                return json;
            } else {
                json.put("code", 2);
                json.put("msg", "上传失败，有同名文件");
                json.put("data", "{'file':'" + file.getOriginalFilename() + "'}");
                return json;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        json.put("code", -1);
        json.put("msg", "上传失败");
        json.put("data", "{'file':'" + file.getOriginalFilename() + "'}");*/
        return json;
    }


    /**
     * 学生端搜索教师（未完成）
     */
    @RequestMapping(value = "/searchTeacher", method = RequestMethod.POST)
    public Map searchTeacher(@RequestParam String key, @RequestParam String keyword) {
        HashMap<String, Object> des;
        if (BasicConstant.User.USERNAME.getString().equals(key)) {
            des = userService.selectUser(keyword);
        } else {
            des = userService.selectUserByRealname(keyword);
        }
        if (des.get("user") == null) {
            des.put("code", 0);
            des.put("msg", "not found teacher");
        } else {

            des.put("code", 0);
            des.put("msg", "success");
        }
        return des;
    }
}
