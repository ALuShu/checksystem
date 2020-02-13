package com.lushu.checksystem.service;

import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.PageBean;

import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 * @throws
 * @since
 */
public interface InformService {
    List<Inform> selectInforms(Integer sendId);
    PageBean<Inform> selectAllInforms(int currentPage);
    List<Inform> selectInformsByType(boolean type);
    Integer addInforms(List<Inform> list);
    Integer updateInform(Inform inform);
    Integer deleteInforms(List<Integer> ids);
}
