package com.lushu.checksystem.dao;

import com.lushu.checksystem.pojo.Authority;
import com.lushu.checksystem.pojo.Role;
import com.lushu.checksystem.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
     * 根据用户名搜索用户
     * @param username
     * @return
     */
    User selectUserByUsername(String username);

    /**
     * 根据学号或工号群，查询用户群
     * @param username
     * @return
     */
    List<User> selectUsersByUsername(List<String> username);

    /**
     * id搜索用户（测试用）
     * @param id
     * @return
     */
    User selectById(Integer id);

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
     * 根据角色搜索用户群（用户与角色n:1）
     * @param role
     * @return
     */
    List<User> selectUsersByRole(Integer role);

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

    /**
     * 批量添加用户角色中间表记录（用户与角色n:1）
     * @param userId
     * @param roleId
     * @return
     */
    int addUserRole(@Param("userId") List<Integer> userId, @Param("roleId") Integer roleId);

    /**
     * 批量删除用户角色中间表记录（用户与角色n:1）
     * @param userIds
     * @return
     */
    int deleteUserRole(List<Integer> userIds);

    /**
     * 更新用户角色中间表记录（用户与角色n:1）
     * @param userId
     * @param roleId
     * @return
     */
    int updateUserRole(@Param("userId")Integer userId, @Param("roleId")Integer roleId);
}
