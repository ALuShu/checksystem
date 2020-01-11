package com.lushu.checksystem.dao;

import com.lushu.checksystem.pojo.Authority;
import com.lushu.checksystem.pojo.Role;
import com.lushu.checksystem.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ALuShu
 * @date 2020/1/10
 * @throws
 * @since
 * @Description 用户持久层接口
 */
@Repository
@Mapper
public interface UserDao {

    /**
     * 查询用户列表
     * @return
     */
    List<User> selectAllUser();

    /**
     * 用户名搜索用户
     * @param username
     * @return
     */
    User selectUser(String username);

    /**
     * 用户名搜索用户的角色
     * @param username
     * @return
     */
    Role selectRoleByUsername(String username);

    /**
     * 用户名搜索用户的全部权限，用于登录认证
     * @param username
     * @return
     */
    List<Authority> selectAuthoritiesByUsername(String username);

    /**
     * 搜索总记录数
     * @return
     */
    int countUsers();

    /**
     * 批量增加用户
     * @param users
     * @return
     */
    int addUsers(List<User> users);

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    int deleteUsers(List<Integer> ids);

    /**
     * 批量更新用户
     * @param users
     * @return
     */
    int updateUsers(List<User> users);

    /**
     * 修改密码
     * @param user
     * @return
     */
    int updatePassword(User user);
}
