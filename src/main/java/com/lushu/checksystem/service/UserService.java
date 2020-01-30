package com.lushu.checksystem.service;

import com.lushu.checksystem.pojo.Authority;
import com.lushu.checksystem.pojo.Role;
import com.lushu.checksystem.pojo.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 * @throws
 * @since
 */
public interface UserService extends UserDetailsService {
    List<User> selectAllUser();
    List<User> selectUsersByUsername(List<String> username);
    User selectById(Integer id);
    User selectUser(String username);
    Role selectRoleByUsername(String username);
    List<Authority> selectAuthoritiesByUsername(String username);
    List<User> selectUsersByRole(Integer role);

    /**
     * 继承UserDetailsService的方法
     */
    @Override
    UserDetails loadUserByUsername(String var1) throws UsernameNotFoundException;

    int countUsers();
    int addUsers(List<User> users);
    int deleteUsers(List<Integer> ids);
    int updateUsers(List<User> users);
    int updatePassword(User user);
    int addUserRole(List<Integer> userId, Integer roleId);
    int deleteUserRole(List<Integer> userIds);
    int updateUserRole(Integer userId, Integer roleId);
}
