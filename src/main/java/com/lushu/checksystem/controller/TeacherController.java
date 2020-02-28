package com.lushu.checksystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lushu.checksystem.constant.BasicConstant;
import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.PageBean;
import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.FileService;
import com.lushu.checksystem.service.InformService;
import com.lushu.checksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lushu
 * @date 19-11-13 下午1:49
 **/
@Controller
@Slf4j
@RequestMapping("/teacher")
public class TeacherController {

    private String root;
    private User user = new User();
    private FileService fileService;
    private UserService userService;
    private InformService informService;

    public TeacherController(FileService fileService, UserService userService, InformService informService) {
        this.fileService = fileService;
        this.userService = userService;
        this.informService = informService;
    }

    /**
     * 教师页面跳转
     */
    @RequestMapping("/index")
    public String index(Model model){
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            root = user.getUsername() + "_" + user.getRealname() + "/";
            return "/teacher/index";
        }
    }
    @RequestMapping("/edit")
    public String inform(Model model){
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/teacher/edit";
        }
    }
    @RequestMapping("/personal")
    public String personal(Model model){
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/teacher/personal";
        }
    }
    @RequestMapping("/update")
    public String update(Model model){
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            User user = (User) a;
            model.addAttribute("current", user);
            return "/teacher/update";
        }
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
     * 教师端展示文件列表
     */
    @RequestMapping(value = "/teacherList", method = RequestMethod.GET)
    @ResponseBody
    public Map list(@RequestParam(required = false) int page
            , @RequestParam(required = false) int limit
            , @RequestParam(required = false) String path) {
        Map<String, Object> res = new HashMap<>();
        if (path == null || "/".equals(path)){
            path = root;
        }else {
            path = root + path;
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
     * 教师端上传文件方法（后续优化：多文件上传时不像现在的要请求多次此方法）
     */
    @PostMapping("/uploadFile")
    @ResponseBody
    public Map upload(HttpServletRequest request, @RequestParam("path") String path) {
        Map<String, Object> json = new HashMap<>();
        if (path == null || "/".equals(path)){
            path = root;
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = user.getId();
        if (fileMap == null || fileMap.size() == 0) {
            json.put("code", 0);
            json.put("msg", "请选择文件!");
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
     * 创建文件夹
     */
    @PostMapping("/newFile")
    @ResponseBody
    public Map newFile(@RequestParam String name, @RequestParam String path) {
        Map<String, Object> json = new HashMap<>();
        if (path == null || "/".equals(path)){
            path = root;
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int res = fileService.addDirectory(name, path, user.getId());
        if (res == 1){
            json.put("code", 1);
            json.put("msg", "创建成功！");
            return json;
        }else {
            json.put("code", 0);
            json.put("msg", "创建失败！");
            return json;
        }
    }

    /**
     * 删除文件
     */
    @PostMapping("/delFile")
    @ResponseBody
    public Map delFile(@RequestParam(value = "name[]") String[] name, @RequestParam String path) {
        Map<String, Object> json = new HashMap<>();
        if (path == null || "/".equals(path)){
            path = root;
        }
        HashMap<String, Object> param = new HashMap<>();
        param.put("fileName", name);
        param.put("path", root+path);
        int res = fileService.deleteFile(param);
        if (res == 1){
            json.put("code", 1);
            json.put("msg", "创建成功！");
            return json;
        }else {
            json.put("code", 0);
            json.put("msg", "创建失败！");
            return json;
        }
    }

    /**
     * 重命名文件
     */
    @PostMapping("/renameFile")
    @ResponseBody
    public Map renameFile(@RequestParam String name, @RequestParam String path) {
        Map<String, Object> json = new HashMap<>();
        int index = path.lastIndexOf("/");
        String tmpPath = path.substring(0, index);
        String tmpName = path.substring(index+1);
        HashMap<String, Object> param = new HashMap<>();
        param.put("newName", name);
        param.put("resourcePath", root+tmpPath);
        param.put("resourceName", tmpName);
        int res = fileService.updateFiles(param, BasicConstant.FileAction.RENAME.getString());
        if (res == 1){
            json.put("code", 1);
            json.put("msg", "创建成功！");
            return json;
        }else {
            json.put("code", 0);
            json.put("msg", "创建失败！");
            return json;
        }
    }



    /**
     * 个人中心展示最近批改作业列表owner,最新更新时间
     */
    @RequestMapping(value = "/recentWorks", method = RequestMethod.GET)
    @ResponseBody
    public Map recentWorks(@RequestParam int page, @RequestParam int limit){
        Map<String, Object> json = new HashMap<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageBean<File> pageBean = fileService.selectRecent(user.getId(), page, limit, "update_time");
        json.put("data", pageBean.getList());
        json.put("code", 0);
        json.put("msg", "");
        json.put("count", pageBean.getTotalRecord());
        return json;
    }


    /**
     * 个人中心最近通知
     */
    @RequestMapping(value = "/recentInform", method = RequestMethod.GET)
    @ResponseBody
    public Map recentInform(@RequestParam int limit){
        Map<String, Object> json = new HashMap<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageBean<Inform> pageBean = informService.selectInforms(user.getUsername(), 1, limit);
        json.put("data", pageBean.getList());
        json.put("code", 0);
        json.put("msg", "");
        json.put("count", pageBean.getTotalRecord());
        return json;
    }

    /**
     * 个人中心最近通知(查看更多)
     */
    @RequestMapping(value = "/recentInforms", method = RequestMethod.GET)
    @ResponseBody
    public Map recentInforms(){
        Map<String, Object> json = new HashMap<>();
        return json;
    }


    /**
     * 个人中心最近提交owner,倒数id
     */
    @RequestMapping(value = "/recentSubmit", method = RequestMethod.GET)
    @ResponseBody
    public Map recentSubmit(@RequestParam int limit){
        Map<String, Object> json = new HashMap<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageBean<File> pageBean = fileService.selectRecent(user.getId(), 1, limit, "id");
        json.put("data", pageBean.getList());
        json.put("code", 0);
        json.put("msg", "");
        json.put("count", pageBean.getTotalRecord());
        return json;
    }

    /**
     * 个人中心最近提交(查看更多)
     */
    @RequestMapping(value = "/recentSubmits", method = RequestMethod.GET)
    @ResponseBody
    public Map recentSubmits(){
        Map<String, Object> json = new HashMap<>();
        return json;
    }


    /**
     * 文件下载
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(HttpServletRequest request, String filename) throws IOException {
        String realPath = request.getServletContext().getRealPath("/download");
        java.io.File file = new java.io.File(realPath, filename);
        HttpHeaders headers = new HttpHeaders();
        String downloadFileName = null;
        downloadFileName = new String(filename.getBytes("UTF-8"), "iso-8859-1");
        headers.setContentDispositionFormData("attachment", downloadFileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

    /**
     * 教师端作业查重
     */
    @RequestMapping(value = "/checkFile", method = RequestMethod.POST)
    public void checkFile(@RequestParam(value = "name[]") String[] name, @RequestParam String path){

    }


    /**
     * 教师端作业文件更新
     */
    @RequestMapping(value = "/updateWork", method = RequestMethod.POST)
    public void updateWork(){}





    /**
     * 教师端通知发布
     */
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public void pushInform(){}

}
