package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.constant.BasicConstant;
import com.lushu.checksystem.constant.DatabaseConstant;
import com.lushu.checksystem.constant.OtherConstant;
import com.lushu.checksystem.dao.AuthorityDao;
import com.lushu.checksystem.dao.RoleDao;
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
    private RoleDao roleDao;
    private AuthorityDao authorityDao;
    private FileService fileService;

    public UserServiceImpl(UserDao userDao, RoleDao roleDao, AuthorityDao authorityDao, FileService fileService) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.authorityDao = authorityDao;
        this.fileService = fileService;
    }

    @Override
    public User selectUserBySort(String type, String keyword){
        HashMap<String, Object> pageMap = new HashMap<>(2);
        if (BasicConstant.User.USERNAME.getString().equals(type)){
            User user = userDao.selectUserByUsername(keyword);
            return user;
        }else if (BasicConstant.User.REAL_NAME.getString().equals(type)){
            User user = userDao.selectUserByRealname(keyword);
            return user;
        }else {
            return new User();
        }
    }

    @Override
    public HashMap<String, Object> selectUser(String username) {
        User user = userDao.selectUserByUsername(username);
        List<Role> role = roleDao.selectRole(username);
        HashMap<String, Object> res = new HashMap<>(4);
        res.put("user", user);
        res.put("role", role);
        return res;
    }

    @Override
    public HashMap<String, Object> selectUserByRealname(String realname) {
        User user = userDao.selectUserByRealname("%"+realname+"%");
        List<Role> role = roleDao.selectRole(user.getUsername());
        HashMap<String, Object> res = new HashMap<>(4);
        res.put("user", user);
        res.put("role", role);
        return res;
    }

    @Override
    public List<Authority> selectAuthoritiesByUsername(String username) {
        return authorityDao.selectAuthoritiesByUsername(username);
    }

    @Override
    public Role selectRoleByUsername(String username) {
        //用为角色和用户逻辑上是多对多关系，但此系统暂时是一对一，数据库即持久层的设计遵从多对多
        return roleDao.selectRole(username).get(0);
    }

    @Override
    public List<Role> selectAllRole() {
        return roleDao.selectAllRole();
    }

    @Override
    public List<Authority> selectAllAuthority() {
        return authorityDao.selectAllAuthotities();
    }

    @Override
    public PageBean<User> selectUsersByRole(Integer currentPage, Integer pageSize, Integer roleId) {
        HashMap<String, Object> pageMap = new HashMap<>(3);
        PageBean<User> pageBean = new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        pageMap.put("start", (currentPage - 1) * pageSize);
        pageMap.put("limit", pageBean.getPageSize());
        if (roleId != null) {
            pageMap.put("roleId", roleId);
        }
        List<User> users = userDao.selectUsersByRole(pageMap);
        pageBean.setList(users);
        int count = 0;
        if(roleId != null){
            count = userDao.countCurrentUsers(roleId);
            pageBean.setTotalRecord(count);
        }else {
            count = userDao.countUsers();
            pageBean.setTotalRecord(count);
        }
        if (count % pageSize == 0) {
            pageBean.setTotalPage(count / pageSize);
        } else {
            pageBean.setTotalPage((count / pageSize) + 1);
        }
        return pageBean;
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
        if (user != null) {
            //再根据用户名查角色
            List<Role> roles = roleDao.selectRole(var1);
            List<Authority> authorities = new ArrayList<>();
            List<GrantedAuthority> authorityList = new ArrayList<>();
            for (Role role : roles) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
                authorityList.add(grantedAuthority);

                authorities = role.getAuthorityList();
                for (Authority authority : authorities) {
                    grantedAuthority = new SimpleGrantedAuthority(authority.getTag());
                    authorityList.add(grantedAuthority);
                }
            }

            //把该用户的角色和权限放User对象
            user.setAuthorities(authorityList);
            return user;
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public int countUsers() {
        return userDao.countUsers();
    }

    @Override
    public HashMap<String, Object> addUsersByExcel(List<User> users, Integer roleId) {
        HashMap<String, Object> res = new HashMap<>(2);
        if (users.size() == 0){
            log.info("用户集合为空,请检查参数是否正确输入");
            return res;
        }else {
            //这里先把待添加的用户做一次遍历，若已有相同用户记录则移至另一用户集合existUsers，后面一起返回
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
            //若检查完待添加的用户集合已空，则直接返回existUsers
            if (users.size() == 0) {
                res.put("exist",existUsers);
                return res;
            } else {
                //开始就传进来的角色编号roleId做添加操作
                List<Integer> ids = new ArrayList<>();
                if (roleId == DatabaseConstant.Role.ROLE_TEACHER.ordinal()+1) {
                    //初始化密码和日期
                    for (User user : users) {
                        user.setPassword(new BCryptPasswordEncoder().encode("111111"));
                        user.setCreateTime(OtherConstant.DATE_FORMAT.format(new Date()));
                    }
                    //用户表添加操作,获得自增长返回的id
                    Integer userRes = userDao.addUsers(users);
                    for (User user:users){
                        ids.add(user.getId());
                        int fileRes = fileService.newTeacherFile(user.getUsername(), user.getRealname(), user.getId());
                        if (fileRes != 1) {
                            log.info("教师-" + user.getRealname() + "-文件夹已存在");
                        }
                    }
                    //中间表添加操作
                    Integer midRes = userDao.addUserRole(ids, roleId);
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
                } else if (roleId == DatabaseConstant.Role.ROLE_STUDENT.ordinal()+1) {
                    //初始化密码和日期
                    for (User user : users) {
                        user.setPassword(new BCryptPasswordEncoder().encode("111111"));
                        user.setCreateTime(OtherConstant.DATE_FORMAT.format(new Date()));
                    }
                    //用户表添加操作,获得自增长返回的id
                    Integer userRes = userDao.addUsers(users);
                    for (User user:users){
                        ids.add(user.getId());
                    }
                    //中间表添加操作
                    Integer midRes = userDao.addUserRole(ids, roleId);
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
                        user.setPassword(new BCryptPasswordEncoder().encode("root"));
                        user.setRealname("系统管理员");
                        user.setCreateTime(OtherConstant.DATE_FORMAT.format(new Date()));
                    }
                    Integer userRes = userDao.addUsers(users);
                    for (User user:users){
                        ids.add(user.getId());
                    }
                    Integer midRes = userDao.addUserRole(ids, roleId);
                    res.put("res", (userRes + midRes));
                    return res;
                }
            }
        }
    }

    @Override
    public int deleteUsers(List<Integer> ids) {
        int delUserRes = userDao.deleteUsers(ids);
        int delRoleRes = userDao.deleteUserRole(ids);
        return (delUserRes + delRoleRes);
    }

    @Override
    public int updateUsers(List<User> users) {
        return userDao.updateUsers(users);
    }

    @Override
    public int updatePassword(String newPassword, String oldPassword, User oldUser) {
        int res = 0;
        String encodedPassword = userDao.selectUserByUsername(oldUser.getUsername()).getPassword();
        if (BCryptPasswordEncoder().matches(oldPassword, encodedPassword)){
            User user = new User();
            user.setId(oldUser.getId());
            user.setPassword(BCryptPasswordEncoder().encode(newPassword));
            res = userDao.updatePassword(user);
        }else {
            res = -1;
        }
        return res;
    }

    private static BCryptPasswordEncoder BCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
