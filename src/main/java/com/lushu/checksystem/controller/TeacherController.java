package com.lushu.checksystem.controller;

import com.lushu.checksystem.constant.BasicConstant;
import com.lushu.checksystem.pojo.*;
import com.lushu.checksystem.service.FileService;
import com.lushu.checksystem.service.InformService;
import com.lushu.checksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/teacher")
public class TeacherController {

    private StringBuffer root;
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
    public String index(Model model) {
        Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())) {
            return "redirect:/logout";
        } else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/teacher/index";
        }
    }

    @RequestMapping("/personal")
    public String personal(Model model) {
        Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())) {
            return "redirect:/logout";
        } else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/teacher/personal";
        }
    }

    @RequestMapping("/update")
    public String update(Model model) {
        Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())) {
            return "redirect:/logout";
        } else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/teacher/update";
        }
    }

    @RequestMapping("/informs")
    public String informs(Model model) {
        Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(a.toString())) {
            return "redirect:/logout";
        } else {
            user = (User) a;
            model.addAttribute("current", user);
            return "/teacher/informs";
        }
    }


    /**
     * 修改密码（后续优化：将旧密码的确认做成异步）
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public Map updatePassword(@RequestBody Map jsonUsers) {
        Map<String, Object> res = new HashMap<>(2);
        String oldPassword = (String) jsonUsers.get("oldPassword");
        String newPassword = (String) jsonUsers.get("newPassword");
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int updRes = userService.updatePassword(newPassword, oldPassword, user);
        if (updRes == 0) {
            res.put("code", 0);
            res.put("msg", "error");
        } else if (updRes == -1) {
            res.put("code", -1);
            res.put("msg", "old password error");
        } else {
            res.put("code", 1);
            res.put("msg", "success");
        }
        return res;
    }


    /**
     * 重命名文件
     */
    @PostMapping("/renameFile")
    @ResponseBody
    public Map renameFile(@RequestParam String name, @RequestParam String path) {
        Map<String, Object> json = new HashMap<>();
        path = root + path;
        int index = path.lastIndexOf("\\");
        String tmpPath;
        if (index == path.indexOf("\\") + 1) {
            tmpPath = path.substring(0, index);
        } else {
            tmpPath = path.substring(0, index) + "\\";
        }
        String tmpName = path.substring(index + 1);
        HashMap<String, Object> param = new HashMap<>();
        param.put("newName", name);
        param.put("resourcePath", tmpPath);
        param.put("resourceName", tmpName);
        int res = fileService.updateFiles(param, BasicConstant.FileAction.RENAME.getString());
        if (res == 1) {
            json.put("code", 1);
            json.put("msg", "创建成功！");
            return json;
        } else {
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
    public Map recentWorks(@RequestParam int page, @RequestParam int limit) {
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
    public Map recentInform(@RequestParam int limit) {
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
     * 个人中心最近提交owner,倒数id
     */
    @RequestMapping(value = "/recentSubmit", method = RequestMethod.GET)
    @ResponseBody
    public Map recentSubmit(@RequestParam int limit) {
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
     * 教师端通知发布
     */
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    @ResponseBody
    public Map pushInform(Inform inform) {
        HashMap<String, Object> resMap = new HashMap<>();
        List<Inform> informs = new ArrayList<>();
        informs.add(inform);
        Integer updRes = informService.addInforms(informs);
        if (updRes == 1) {
            resMap.put("code", 0);
            resMap.put("msg", "发布成功");
        } else {
            resMap.put("code", 1);
            resMap.put("msg", "发布失败");
        }
        return resMap;
    }

    /**
     * 教师端通知编辑
     */
    @RequestMapping(value = "/editInform", method = RequestMethod.POST)
    @ResponseBody
    public Map editInform(Inform inform) {
        HashMap<String, Object> resMap = new HashMap<>();
        Integer updRes = informService.updateInform(inform);
        if (updRes == 1) {
            resMap.put("code", 0);
            resMap.put("msg", "更新成功");
        } else {
            resMap.put("code", 1);
            resMap.put("msg", "更新失败");
        }
        return resMap;
    }

    /**
     * 教师端通知删除
     */
    @RequestMapping(value = "/delInform", method = RequestMethod.POST)
    @ResponseBody
    public Map delInform(@RequestParam(name = "idParam[]") String[] idParam) {
        HashMap<String, Object> resMap = new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        for (String id : idParam) {
            ids.add(Integer.valueOf(id));
        }
        int delRes = informService.deleteInforms(ids);
        if (delRes == idParam.length) {
            resMap.put("code", 0);
            resMap.put("msg", "删除成功");
        } else if (delRes < idParam.length && delRes > 0) {
            resMap.put("code", 1);
            resMap.put("msg", "删除成功，部分删除不成功");
        } else {
            resMap.put("code", -1);
            resMap.put("msg", "删除失败");
        }
        return resMap;
    }

    /**
     * 个人中心最近通知(查看更多)
     */
    @RequestMapping(value = "/recentInforms", method = RequestMethod.GET)
    @ResponseBody
    public Map recentInforms(@RequestParam int page, @RequestParam int limit) {
        Map<String, Object> res = new HashMap<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageBean<Inform> informs = informService.selectInforms(user.getUsername(), page, limit);
        res.put("code", 0);
        res.put("count", informs.getTotalRecord());
        res.put("data", informs.getList());
        res.put("msg", "");
        return res;
    }


    /**
     * 个人中心最近提交(查看更多)
     */
    @RequestMapping(value = "/recentSubmits", method = RequestMethod.GET)
    @ResponseBody
    public Map recentSubmits() {
        Map<String, Object> json = new HashMap<>();
        return json;
    }


    /**
     * office展示
     */
    @RequestMapping(value = "/showOffice", method = RequestMethod.POST)
    public void showOffice() {
    }

}
