package com.lushu.checksystem.dao;

import com.lushu.checksystem.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/2/14
 * @throws
 * @since
 */

@Repository
@Mapper
public interface RoleDao {
    /**
     * 用户名查角色，多对多包括权限
     * @param username
     * @return
     */
    List<Role> selectRole(String username);
}
