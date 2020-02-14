package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ALuShu
 * @date 2020/1/10
 * @throws
 * @since
 * @Description 角色实体类
 */
@Data
public class Role implements Serializable {

    private Integer id;
    private String name;
    private String detail;

    private List<Authority> authorityList;
}
