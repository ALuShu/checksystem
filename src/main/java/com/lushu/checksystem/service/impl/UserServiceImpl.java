package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.dao.UserDao;
import com.lushu.checksystem.pojo.Authority;
import com.lushu.checksystem.pojo.Role;
import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> selectAllUser() {
        return userDao.selectAllUser();
    }

    @Override
    public List<User> selectUserByUsername(List<String> username) {
        return userDao.selectUsersByUsername(username);
    }

    @Override
    public User selectById(Integer id) {
        return userDao.selectById(id);
    }

    @Override
    public User selectUser(String username) {
        return userDao.selectUser(username);
    }

    @Override
    public Role selectRoleByUsername(String username) {
        return userDao.selectRoleByUsername(username);
    }

    @Override
    public List<Authority> selectAuthoritiesByUsername(String username) {
        return userDao.selectAuthoritiesByUsername(username);
    }

    @Override
    public List<User> selectUserByRole(Integer role) {
        return userDao.selectUserByRole(role);
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
    public int addUsers(List<User> users) {
        return userDao.addUsers(users);
    }

    @Override
    public int deleteUsers(List<Integer> ids) {
        return userDao.deleteUsers(ids);
    }

    @Override
    public int updateUsers(List<User> users) {
        return userDao.updateUsers(users);
    }

    @Override
    public int updatePassword(User user) {
        return userDao.updatePassword(user);
    }

    @Override
    public int addUserRole(List<Integer> userId, Integer roleId) {
        return userDao.addUserRole(userId,roleId);
    }

    @Override
    public int deleteUserRole(List<Integer> userIds) {
        return userDao.deleteUserRole(userIds);
    }

    @Override
    public int updateUserRole(Integer userId, Integer roleId) {
        return userDao.updateUserRole(userId, roleId);
    }
}
