package com.lushu.checksystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lushu.checksystem.constant.BasicConstant;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author lushu
 * @date 19-11-13 下午1:49
 **/
@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Value("${checksystem.root}")
    private String root;
    private UserService userService;
    private FileService fileService;
    private InformService informService;
    private User user = user = new User();

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
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current",user);
        return "/admin/index";
    }
    @RequestMapping("/authorities")
    public String authorities(Model model){
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current",user);
        return "/admin/authorities";
    }
    @RequestMapping("/files")
    public String files(Model model){
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current",user);
        return "/admin/files";
    }
    @RequestMapping("/informs")
    public String informs(Model model){
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current",user);
        return "/admin/informs";
    }
    @RequestMapping("/roles")
    public String roles(Model model){
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current",user);
        return "/admin/roles";
    }
    @RequestMapping("/search")
    public String search(Model model){
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current",user);
        return "/admin/search";
    }
    @RequestMapping("/students")
    public String students(Model model){
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current",user);
        return "/admin/students";
    }
    @RequestMapping("/teachers")
    public String teachers(Model model){
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("current",user);
        return "/admin/teachers";
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
     * 管理员端展示通知列表
     */
    @RequestMapping(value = "/informType", method = RequestMethod.GET)
    @ResponseBody
    public Map inform(@RequestParam(value = "page", required = false) int page
            ,@RequestParam(value = "limit", required = false) int limit
            ,@RequestParam(required = false, value = "type") Integer type) {
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
     * 管理员端搜索用户（未完成）
     */
    @RequestMapping(value = "/searchUser", method = RequestMethod.POST)
    @ResponseBody
    public Map user(@RequestParam String type, @RequestParam String keyword) throws ServletException, JSONException {
        Map<String, Object> memberMap;
        if (BasicConstant.User.USERNAME.getString().equals(type)){
            memberMap = userService.selectUser(keyword);
        } else if (BasicConstant.User.REAL_NAME.getString().equals(type)) {
            memberMap = userService.selectUserByRealname(keyword);
        }else if (BasicConstant.User.DEPARTMENT.getString().equals(type)){
            memberMap = new HashMap<>();
            memberMap.put("user", userService.selectUsersByDepartment(keyword));
        }else {//major
            memberMap = new HashMap<>();
            memberMap.put("user", userService.selectUsersByMajor(keyword));
        }
        return memberMap;
    }

    /**
     * 管理员端批量管理用户（未完成）
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
        Map<String, Object> addRes = userService.addUsersByExcel(userList, roleId);
        if (!addRes.containsKey("exist")){
            res.put("code", 1);
            res.put("msg","添加成功");
        }else {
            log.info("部分数据录入失败");
            res.put("code", 0);
            res.put("exist", addRes.get("exist"));
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
