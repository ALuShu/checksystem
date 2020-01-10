package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;

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
}
