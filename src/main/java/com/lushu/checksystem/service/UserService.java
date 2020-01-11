package com.lushu.checksystem.service;

import com.lushu.checksystem.pojo.Authority;
import com.lushu.checksystem.pojo.Role;
import com.lushu.checksystem.pojo.User;

import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 * @throws
 * @since
 */
public interface UserService {
    List<User> selectAllUser();
    User selectUser(String username);
    Role selectRoleByUsername(String username);
    List<Authority> selectAuthoritiesByUsername(String username);
    int countUsers();
    int addUsers(List<User> users);
    int deleteUsers(List<Integer> ids);
    int updateUsers(List<User> users);
    int updatePassword(User user);
}
