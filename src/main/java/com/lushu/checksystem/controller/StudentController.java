package com.lushu.checksystem.controller;

import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.FileService;
import com.lushu.checksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lushu
 * @date 19-11-13 下午1:48
 **/
@Controller
@Slf4j
public class StudentController {

    @Value("${checksystem.root}")
    private String root;
    private final UserService userService;
    private final FileService fileService;

    public StudentController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    /**
     * 学生主页的跳转
     */
    @RequestMapping("/student")
    public String studentIndex(){
        return "/index";
    }

    /**
     * 上传页面的跳转
     */
    @RequestMapping("/upload")
    public String upload(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null){
            if (principal instanceof User){
                User currentUser = (User)principal;
                model.addAttribute("current",currentUser);
            }
        }
        return "/upload";
    }

    /**
     * 展示文件列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
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
     * 上传文件方法
     */
    @PostMapping("/uploadFile")
    @ResponseBody
    public Map uploadres(@RequestParam("file")MultipartFile file, @RequestParam("path") String path){
        Map<String, Object> json = new HashMap<>();
        com.lushu.checksystem.pojo.File daoDest = new com.lushu.checksystem.pojo.File();
        if (file.isEmpty()) {
            json.put("code",1);
            json.put("msg","请选择作业文件!");
            json.put("data","{'file':'"+file.getOriginalFilename()+"'}");
            return json;
        }
        File rootPath = new File("src/main/resources/root"+path);
        /*if (!rootPath.exists()){
            rootPath.mkdir();
        }*/
        String fileName = file.getOriginalFilename();
        String filePath = rootPath.getAbsolutePath();
        daoDest.setName(fileName);
        daoDest.setPath(filePath);
        daoDest.setSize(file.getSize());
        File dest = new File(filePath, fileName);
        try {
            if (!dest.exists()) {
                fileService.addFile(daoDest);
                file.transferTo(dest);
                json.put("code", 0);
                json.put("msg", "上传成功！");
                json.put("data", "{'file':'" + file.getOriginalFilename() + "'}");
                return json;
            }else {
                json.put("code", 2);
                json.put("msg", "上传失败，有同名文件");
                json.put("data", "{'file':'" + file.getOriginalFilename() + "'}");
                return json;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        json.put("code",-1);
        json.put("msg","上传失败");
        json.put("data","{'file':'"+file.getOriginalFilename()+"'}");
        return json;
    }

    /**
     * 个人中心的跳转
     */
    @RequestMapping("/STUpersonal")
    public String personal(){
        return "/private";
    }
}
