package com.lushu.checksystem.controller;

import com.lushu.checksystem.pojo.PageBean;
import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ALuShu
 * @Description
 * @date 2020/3/29
 */
@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {

    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 展示文件列表
     */
    @PreAuthorize("hasAnyRole('STUDENT') or hasAnyRole('TEACHER') or hasAnyRole('ADMIN')")
    @GetMapping("/show")
    public Map showList(@RequestParam(required = false) int page
            , @RequestParam(required = false) int limit
            , @RequestParam(required = false) String path) {
        Map<String, Object> res = new HashMap<>();
        User user;
        Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())) {
            res.put("data", null);
            res.put("code", 0);
            res.put("msg", "身份验证过期，请重新登录");
            res.put("count", 0);
        } else {
            user = (User) a;
            List<GrantedAuthority> grantedAuthorities = user.getAuthorities();
            if (grantedAuthorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
                if (path == null) {
                    path = "/";
                }
            }else if (grantedAuthorities.contains(new SimpleGrantedAuthority("ROLE_TEACHER"))){
                if ("\\".equals(path) || "/".equals(path) || path == null) {
                    path = user.getUsername() + "_" + user.getRealname() + "\\";
                } else {
                    path = user.getUsername() + "_" + user.getRealname() + "\\" + path;
                }
            }else {
                StringBuffer current = StudentController.current;
                if ("\\".equals(path) || "/".equals(path) || path == null){
                    path = current.toString();
                }else if ("".equals(current.toString())){
                    path = "/test";
                }else {
                    path = current + path;
                }
            }
            PageBean<Map<String, Object>> fileList = fileService.showFileList(path, page, limit);
            if (fileList == null) {
                res.put("data", null);
                res.put("code", 0);
                res.put("msg", "");
                res.put("count", 0);
            } else {
                res.put("data", fileList.getList());
                res.put("code", 0);
                res.put("msg", "");
                res.put("count", fileList.getTotalRecord());
            }
        }
        return res;
    }

    /**
     * 上传文件
     */
    @PreAuthorize("hasAnyRole('STUDENT') or hasAnyRole('TEACHER') or hasAnyRole('ADMIN')")
    @PostMapping("/uploadFile")
    public Map upload(HttpServletRequest request, @RequestParam("path") String path){
        Map<String, Object> json = new HashMap<>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        if (fileMap == null || fileMap.size() == 0) {
            json.put("code", 0);
            json.put("msg", "请选择文件!");
            return json;
        }
        Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())) {
            json.put("data", null);
            json.put("code", 0);
            json.put("msg", "身份验证过期，请重新登录");
            json.put("count", 0);
        } else {
            User user = (User) a;
            List<GrantedAuthority> grantedAuthorities = user.getAuthorities();
            Integer id = user.getId();
            Collection<MultipartFile> files = fileMap.values();
            if (grantedAuthorities.contains(new SimpleGrantedAuthority("ROLE_TEACHER"))){
                if ("\\".equals(path) || path.equals(user.getUsername() + "_" + user.getRealname() + "\\")) {
                    path = user.getUsername() + "_" + user.getRealname() + "\\";
                } else {
                    path = user.getUsername() + "_" + user.getRealname() + "\\" + path;
                }
            }else if(grantedAuthorities.contains(new SimpleGrantedAuthority("ROLE_STUDENT"))){
                StringBuffer current = StudentController.current;
                if ("\\".equals(path) || "/".equals(path) || path == null){
                    path = current.toString();
                }else if ("".equals(current.toString())){
                    path = "/test";
                }else {
                    path = current + path;
                }
            }
            int res = fileService.addFiles(files, path, id);
            if (res == fileMap.size()) {
                json.put("code", 1);
                json.put("msg", "上传成功！");
            } else if (res == 0) {
                json.put("code", 2);
                json.put("msg", "覆盖操作！");
            } else {
                json.put("code", -1);
                json.put("msg", "上传失败！");
            }
        }
        return json;
    }
}
