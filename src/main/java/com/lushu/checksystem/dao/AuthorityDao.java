package com.lushu.checksystem.dao;

import com.lushu.checksystem.pojo.Authority;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/2/20
 */
@Repository
@Mapper
public interface AuthorityDao {
    /**
     * 用户名搜索用户的全部权限，用于登录认证
     * @param username
     * @return
     */
    List<Authority> selectAuthoritiesByUsername(String username);

    /**
     * 所有权限
     * @return
     */
    List<Authority> selectAllAuthotities();
}
