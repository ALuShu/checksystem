package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ALuShu
 * @date 2020/1/10
 * @throws
 * @since
 * @Description 权限实体类
 */
@Data
public class Authority implements Serializable {
    private Integer id;
    private String name;
    private String tag;
}
