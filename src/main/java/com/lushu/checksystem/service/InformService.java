package com.lushu.checksystem.service;

import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.PageBean;

import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 */
public interface InformService {
    Inform selectInform(Integer id);
    PageBean<Inform> selectInforms(String publisher, int page, int limit);
    PageBean<Inform> selectAllInforms(int currentPage);
    PageBean<Inform> selectInformsBySort(Integer type, int currentPage, int limit, String ... department);
    Integer addInforms(List<Inform> list);
    Integer updateInform(Inform inform);
    Integer deleteInforms(List<Integer> ids);
}
