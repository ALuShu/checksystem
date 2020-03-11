package com.lushu.checksystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lushu.checksystem.pojo.*;
import com.lushu.checksystem.service.FileService;
import com.lushu.checksystem.service.InformService;
import com.lushu.checksystem.service.UserService;
import com.lushu.checksystem.util.ExcelUtil;
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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


/**
 * @author lushu
 * @date 19-11-13 下午1:49
 **/
@Controller
@Slf4j
@RequestMapping("/admin")
@SuppressWarnings("unchecked")
public class AdminController {

    @Value("${checksystem.root}")
    private String root;
    private UserService userService;
    private FileService fileService;
    private InformService informService;
    private User user = new User();

    public AdminController(UserService userService, FileService fileService, InformService informService) {
        this.userService = userService;
        this.fileService = fileService;
        this.informService = informService;
    }

    /**
     * 管理员的页面跳转
     */
    @RequestMapping("/index")
    public String index(Model model){
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/admin/index";
        }
    }
    @RequestMapping("/authorities")
    public String authorities(Model model){
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/admin/authorities";
        }
    }
    @RequestMapping("/files")
    public String files(Model model){
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/admin/files";
        }
    }
    @RequestMapping("/informs")
    public String informs(Model model){
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/admin/informs";
        }
    }
    @RequestMapping("/roles")
    public String roles(Model model){
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/admin/roles";
        }
    }
    @RequestMapping("/search")
    public String search(Model model){
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/admin/search";
        }
    }
    @RequestMapping("/students")
    public String students(Model model){
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/admin/students";
        }
    }
    @RequestMapping("/teachers")
    public String teachers(Model model){
        Object a =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())){
            return "redirect:/logout";
        }else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/admin/teachers";
        }
    }


    /**
     * 管理员端展示文件列表
     */
    @RequestMapping(value = "/adminList", method = RequestMethod.GET)
    @ResponseBody
    public Map list(@RequestParam int page
            , @RequestParam int limit
            , @RequestParam(required = false) String path) throws ServletException, JSONException {
        Map<String, Object> res = new HashMap<>();
        if (path == null) {
            path = "/";
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
     * 管理员端展示用户列表
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public Map users(@RequestParam("page") int page
            ,@RequestParam("limit") int limit
            ,@RequestParam(required = false, value = "role") Integer role){
        Map<String, Object> memberMap = new HashMap<>(4);
        PageBean<User> res = userService.selectUsersByRole(page, limit, role);
        memberMap.put("data",res.getList());
        memberMap.put("code",0);
        memberMap.put("msg","");
        memberMap.put("count",res.getTotalRecord());
        return memberMap;
    }


    /**
     * Excel表格处理器
     */
    @PostMapping("/excelAdd")
    @ResponseBody
    public Map upload(HttpServletRequest request, @RequestParam("role")int role) {
        Map<String, Object> json = new HashMap<>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        List<User> existUsers = new ArrayList<>();
        for (String string : fileMap.keySet()){
            ExcelUtil<User> userExcelUtil = new ExcelUtil<>(User.class);
            MultipartFile currentFile = fileMap.get(string);
            String fileName = currentFile.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            java.io.File upload = new java.io.File("src/main/resources/excel");
            if(!upload.exists()){
                upload.mkdirs();
            }
            fileName = UUID.randomUUID() + suffixName;
            java.io.File dest = new java.io.File(upload.getAbsolutePath() +"/"+ fileName);
            try {
                currentFile.transferTo(dest);
                List<User> studentList = userExcelUtil.explain(dest.getAbsolutePath());
                Map<String, Object> resMap = userService.addUsersByExcel(studentList, role);
                if (resMap.get("exist") != null && resMap.get("exist") instanceof List<?>){
                    existUsers.addAll((List<User>) resMap.get("exist"));
                }
            } catch (IOException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                log.error("解析Excel发生错误，控制器处理异常",e);
                json.put("code", -1);
                json.put("msg", "上传失败！");
                return json;
            }
        }
        if (existUsers.size() == 0){
            json.put("code", 0);
            json.put("msg", "上传成功！");
            return json;
        }else {
            json.put("code", 1);
            json.put("exist", existUsers);
            json.put("msg", "上传成功！");
            return json;
        }
    }

    /**
     * 管理员端展示通知列表
     */
    @RequestMapping(value = "/informType", method = RequestMethod.GET)
    @ResponseBody
    public Map inform(@RequestParam int page
            ,@RequestParam int limit
            ,@RequestParam(required = false) Integer type) {
        Map<String, Object> informMap = new HashMap<>(4);
        PageBean<Inform> res = informService.selectInformsBySort(type, page, limit);
        informMap.put("data",res.getList());
        informMap.put("code",0);
        informMap.put("msg","");
        informMap.put("count",res.getTotalRecord());
        return informMap;
    }


    /**
     * 管理员端展示角色列表
     */
    @RequestMapping(value = "/showRoles",method = RequestMethod.GET)
    @ResponseBody
    public Map roles(@RequestParam(value = "username", required = false) String username){
        Map<String, Object> roleMap = new HashMap<>(4);
        if (username == null) {
            List<Role> roles = userService.selectAllRole();
            roleMap.put("data", roles);
            roleMap.put("code", 0);
            roleMap.put("msg", "");
            roleMap.put("count", roles.size());
        }else {
            Role role = userService.selectRoleByUsername(username);
            roleMap.put("data", role);
            roleMap.put("code", 0);
            roleMap.put("msg", "");
            roleMap.put("count", 1);
        }
        return roleMap;
    }

    /**
     * 管理员端展示角色列表
     */
    @RequestMapping(value = "/showAuthorities",method = RequestMethod.GET)
    @ResponseBody
    public Map authority(@RequestParam(value = "username", required = false) String username){
        Map<String, Object> authorityMap = new HashMap<>(4);
        if (username == null) {
            List<Authority> authorities = userService.selectAllAuthority();
            authorityMap.put("data", authorities);
            authorityMap.put("code", 0);
            authorityMap.put("msg", "");
            authorityMap.put("count", authorities.size());
        }else {
            List<Authority> authorities = userService.selectAuthoritiesByUsername(username);
            authorityMap.put("data", authorities);
            authorityMap.put("code", 0);
            authorityMap.put("msg", "");
            authorityMap.put("count", authorities.size());
        }
        return authorityMap;
    }


    /**
     * 管理员端搜索用户
     */
    @RequestMapping("/search/{type}/{keyword}")
    @ResponseBody
    public Map searchRes(@PathVariable String type, @PathVariable String keyword){
        HashMap<String, Object> res = new HashMap<>();
        User user = userService.selectUserBySort(type, keyword);
        List<User> users = new ArrayList<>();
        users.add(user);
        if (user == null){
            res.put("code",1);
            res.put("msg","用户不存在");
            res.put("count",0);
            res.put("data",users);
            return res;
        }else {
            res.put("code",0);
            res.put("msg","");
            res.put("count",1);
            res.put("data",users);
            return res;
        }
    }

    /**
     * 管理员端批量管理用户
     */
    @RequestMapping(value = "/manageUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map addUsers(@RequestBody String jsonUsers, @RequestParam Integer roleId) throws InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        Map<String, Object> res = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(jsonUsers, User.class);
        List<User> singleUser = new ArrayList<>();
        singleUser.add(user);
        Map<String, Object> addRes = userService.addUsersByExcel(singleUser, roleId);
        if (addRes.get("exist") != null){
            res.put("msg","该用户已存在");
        }else {
            res.put("msg","添加成功");
        }
        res.put("code",0);
        return res;
    }
    @RequestMapping(value = "/deleteUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map deleteUsers(@RequestBody String jsonUsers) throws JsonProcessingException {
        Map<String, Object> res = new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        if(jsonUsers.startsWith("[")){
            ObjectMapper mapper = new ObjectMapper();
            List<User> beanList = mapper.readValue(jsonUsers, new TypeReference<List<User>>() {});
            for (User user : beanList){
                ids.add(user.getId());
            }
        }else {
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(jsonUsers, User.class);
            ids.add(user.getId());
        }
        Integer delRes = userService.deleteUsers(ids);
        res.put("code",1);
        res.put("count",delRes);
        return res;
    }
    @RequestMapping(value = "/updateUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map updateUsers(@RequestBody String jsonUsers, @RequestParam(required = false) Integer roleId) throws JsonProcessingException {
        Map<String, Object> res = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(jsonUsers, User.class);
        List<User> users = new ArrayList<>();
        users.add(user);
        Integer updRes = userService.updateUsers(users);
        res.put("code",1);
        res.put("count",updRes);
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
