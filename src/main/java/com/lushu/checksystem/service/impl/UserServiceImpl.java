package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.constant.DatabaseConstant;
import com.lushu.checksystem.constant.OtherConstant;
import com.lushu.checksystem.dao.UserDao;
import com.lushu.checksystem.pojo.Authority;
import com.lushu.checksystem.pojo.PageBean;
import com.lushu.checksystem.pojo.Role;
import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.FileService;
import com.lushu.checksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 * @throws
 * @since
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private FileService fileService;

    public UserServiceImpl(UserDao userDao, FileService fileService) {
        this.userDao = userDao;
        this.fileService = fileService;
    }

    @Override
    public PageBean<User> selectAllUser(Integer currentPage, Integer pageSize) {
        HashMap<String, Object> pageMap = new HashMap<>(2);
        PageBean<User> pageBean = new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        int count = userDao.countUsers();
        pageBean.setTotalRecord(count);
        if (count % pageSize == 0){
            pageBean.setTotalPage(count/pageSize);
        }else {
            pageBean.setTotalPage((count/pageSize) + 1);
        }
        pageMap.put("start", (currentPage-1) * pageSize);
        pageMap.put("limit", pageBean.getPageSize());
        pageBean.setList(userDao.selectAllUser(pageMap));
        return pageBean;
    }

    @Override
    public HashMap<String, Object> selectUser(String username) {
        User user = userDao.selectUserByUsername(username);
        Role role = userDao.selectRoleByUsername(username);
        HashMap<String, Object> res = new HashMap<>(4);
        res.put("user", user);
        res.put("role", role);
        return res;
    }

    @Override
    public HashMap<String, Object> selectUserByRealname(String realname) {
        User user = userDao.selectUserByRealname("%"+realname+"%");
        Role role = userDao.selectRoleByUsername(user.getUsername());
        HashMap<String, Object> res = new HashMap<>(4);
        res.put("user", user);
        res.put("role", role);
        return res;
    }

    @Override
    public List<Authority> selectAuthoritiesByUsername(String username) {
        return userDao.selectAuthoritiesByUsername(username);
    }

    @Override
    public Role selectRoleByUsername(String username) {
        return userDao.selectRoleByUsername(username);
    }

    @Override
    public List<User> selectUsersByRole(Integer role) {
        return userDao.selectUsersByRole(role);
    }

    @Override
    public List<User> selectUsersByDepartment(String department) {
        return userDao.selectUsersByDepartment(department);
    }

    @Override
    public List<User> selectUsersByMajor(String major) {
        return userDao.selectUsersByMajor(major);
    }

    @Override
    public UserDetails loadUserByUsername(String var1) throws UsernameNotFoundException {
        //先根据用户名查用户
        User user = userDao.selectUserByUsername(var1);
        //再根据用户名查权限
        List<Authority> authorities = userDao.selectAuthoritiesByUsername(var1);

        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (Authority authority : authorities){
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getTag());
            authorityList.add(grantedAuthority);
        }

        //把该用户的所有权限放User对象
        user.setAuthorities(authorityList);
        return user;
    }

    @Override
    public int countUsers() {
        return userDao.countUsers();
    }

    @Override
    public HashMap<String, Object> addUsersByExcel(List<User> users, Integer roleId) {
        HashMap<String, Object> res = new HashMap<>(2);
        if (users.size() == 0){
            log.error("用户集合为空,请检查参数是否正确输入");
            return res;
        }else {
            Iterator<User> userIterator = users.iterator();
            List<String> oldRecord = userDao.checkUsernames();
            List<User> existUsers = new ArrayList<>();
            while (userIterator.hasNext()) {
                User tmp = userIterator.next();
                int isExist = Collections.binarySearch(oldRecord, tmp.getUsername());
                if (isExist >= 0) {
                    existUsers.add(tmp);
                    userIterator.remove();
                }
            }
            if (users.size() == 0) {
                res.put("exist",existUsers);
                return res;
            } else {
                int firstId;
                if (null != userDao.selectLastId()) {
                    firstId = userDao.selectLastId() + 1;
                } else {
                    firstId = 1;
                }
                List<Integer> ids = new ArrayList<>();
                if (roleId == DatabaseConstant.Role.TEACHER.ordinal()+1) {
                    for (User user : users) {
                        int fileRes = fileService.newTeacherFile(user.getUsername(), user.getRealname(), firstId);
                        if (fileRes != 1) {
                            log.error("教师-" + user.getRealname() + "-文件夹已存在");
                        }
                        ids.add(firstId);
                        firstId++;
                        user.setPassword(new BCryptPasswordEncoder().encode("111111"));
                        user.setCreateTime(OtherConstant.DATE_FORMAT.format(new Date()));
                    }
                    int userRes = userDao.addUsers(users);
                    int midRes = userDao.addUserRole(ids, roleId);
                    res.put("res", (userRes + midRes));
                    if (existUsers.size() == 0) {
                        return res;
                    } else {
                        res.put("exist", existUsers);
                        for (User user : existUsers) {
                            log.error("教师-" + user.getRealname() + "-注册失败，已有相同记录");
                        }
                        return res;
                    }
                } else if (roleId == DatabaseConstant.Role.STUDENT.ordinal()+1) {
                    for (User user : users) {
                        ids.add(firstId);
                        firstId++;
                        user.setPassword(new BCryptPasswordEncoder().encode("111111"));
                        user.setCreateTime(OtherConstant.DATE_FORMAT.format(new Date()));
                    }
                    int userRes = userDao.addUsers(users);
                    int midRes = userDao.addUserRole(ids, roleId);
                    res.put("res", (userRes + midRes));
                    if (existUsers.size() == 0) {
                        return res;
                    } else {
                        res.put("exist", existUsers);
                        for (User user : existUsers) {
                            log.error("学生-" + user.getRealname() + "-注册失败，已有相同记录");
                        }
                        return res;
                    }
                } else {
                    for (User user : users) {
                        ids.add(firstId);
                        user.setUsername("root");
                        user.setPassword(new BCryptPasswordEncoder().encode("root"));
                        user.setRealname("系统管理员");
                        user.setCreateTime(OtherConstant.DATE_FORMAT.format(new Date()));
                    }
                    int userRes = userDao.addUsers(users);
                    int midRes = userDao.addUserRole(ids, roleId);
                    res.put("res", (userRes + midRes));
                    return res;
                }
            }
        }
    }

    @Override
    public int deleteUsers(List<Integer> ids) {
        List<User> users = userDao.selectByIds(ids);
        for (User user : users){
            if (user.getMajor() == null){
                //删除对应的文件和数据库文件记录
            }
        }
        int delUserRes = userDao.deleteUsers(ids);
        int delRoleRes = userDao.deleteUserRole(ids);
        return (delUserRes + delRoleRes);
    }

    @Override
    public int updateUsers(List<User> users) {
        return userDao.updateUsers(users);
    }

    @Override
    public int updatePassword(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userDao.updatePassword(user);
    }
}
