package com.lushu.checksystem.controller;

import com.lushu.checksystem.service.FileService;
import com.lushu.checksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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

    private UserService userService;
    private FileService fileService;
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping("/student")
    public ModelAndView studentIndex(ModelAndView modelAndView){
        modelAndView.setViewName("/index");
        return modelAndView;
    }

    @RequestMapping("/upload")
    public String upload(){
        return "/upload";
    }

    /**
     * 展示文件列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Map list(@RequestParam(required = false) String page,
                    @RequestParam(required = false) String limit,
                    @RequestParam(required = false) String path) throws ServletException, JSONException {
        long star = System.currentTimeMillis();
        Map<String, Object> res = new HashMap<>();
        log.info("path参数是否为空："+ (path));
        if (path == null){
            path = "/";
        }else {
            int index = path.indexOf("undefined");
            if (index == -1){
                path = path.substring(0, path.length()-1);
            }else {
                path = path.substring(0, index);
            }
        }
        log.info("当前页数:"+page+";每页记录数:"+limit);

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
            res.put("code",0);
            res.put("msg","数据获取错误");
            res.put("count",0);
            res.put("data","");
            return res;
        }
    }

    @PostMapping("/uploadFile")
    @ResponseBody
    public String uploadres(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        String fileName = file.getOriginalFilename();
        String filePath = request.getServletContext().getRealPath("upload");
        File dest = new File(filePath, UUID.randomUUID().toString()+fileName);
        try {
            file.transferTo(dest);
            return "上传成功";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败！";
    }



    @RequestMapping("/personal")
    public ModelAndView personal(ModelAndView modelAndView){
        modelAndView.setViewName("/private");
        return modelAndView;
    }
}
