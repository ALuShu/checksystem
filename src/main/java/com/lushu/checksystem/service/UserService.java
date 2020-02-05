package com.lushu.checksystem.service;

import com.lushu.checksystem.pojo.Authority;
import com.lushu.checksystem.pojo.PageBean;
import com.lushu.checksystem.pojo.Role;
import com.lushu.checksystem.pojo.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 * @throws
 * @since
 */
public interface UserService extends UserDetailsService {

    /**
     * 分页查询用户表
     */
    PageBean<User> selectAllUser(Integer currentPage, Integer pageSize);

    /**
     * 通过用户名查询，返回用户以及角色信息
     */
    HashMap<String, Object> selectUser(String username);

    /**
     * 通过姓名模糊查询，返回用户以及角色信息
     */
    HashMap<String, Object> selectUserByRealname(String realname);

    /**
     * 返回用户的权限，为框架服务
     * 根据用户名返回角色，为框架服务
     */
    List<Authority> selectAuthoritiesByUsername(String username);
    Role selectRoleByUsername(String username);

    /**
     * 三个如同名字的方法
     */
    List<User> selectUsersByRole(Integer role);
    List<User> selectUsersByDepartment(String department);
    List<User> selectUsersByMajor(String major);

    /**
     * 继承UserDetailsService的方法
     */
    @Override
    UserDetails loadUserByUsername(String var1) throws UsernameNotFoundException;

    /**
     * 用户表总记录数
     */
    int countUsers();

    /**
     * 批量录入用户，用户已存在的会不录入并返回，包括账户已存在和教师文件夹，其他人录入
     */
    HashMap<String, Object> addUsersByExcel(List<User> users, Integer roleId);

    /**
     * 批量删除用户，并删除中间表记录，若是教师，还要删除其文件夹
     */
    int deleteUsers(List<Integer> ids);

    /**
     * 批量更新用户
     */
    int updateUsers(List<User> users);

    /**
     * 修改密码
     */
    int updatePassword(User user);
}
