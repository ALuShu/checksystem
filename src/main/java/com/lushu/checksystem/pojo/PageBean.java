package com.lushu.checksystem.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/2/3
 * @throws
 * @since
 */
@Data
public class PageBean<T> {
    private Integer totalRecord;
    private Integer totalPage;
    private Integer currentPage;
    private Integer pageSize;
    private List<T> list;
}
