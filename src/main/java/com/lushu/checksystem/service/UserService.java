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
    PageBean<User> selectAllUser(Integer currentPage, Integer pageSize);
    User selectById(Integer id);
    Role selectRoleByUsername(String username);
    HashMap<String, Object> selectUser(String username);
    HashMap<String, Object> selectUserByRealname(String realname);
    List<Authority> selectAuthoritiesByUsername(String username);
    List<User> selectUsersByRole(Integer role);
    List<User> selectUsersByDepartment(String department);
    List<User> selectUsersByMajor(String major);

    /**
     * 继承UserDetailsService的方法
     */
    @Override
    UserDetails loadUserByUsername(String var1) throws UsernameNotFoundException;

    int countUsers();
    int addUsersByExcel(List<User> users, Integer roleId);
    int deleteUsers(List<Integer> ids);
    int updateUsers(List<User> users);
    int updatePassword(User user);
    int updateUserRole(Integer userId, Integer roleId);
}
