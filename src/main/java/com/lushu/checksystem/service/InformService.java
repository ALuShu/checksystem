package com.lushu.checksystem.service;

import com.lushu.checksystem.pojo.Inform;

import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 * @throws
 * @since
 */
public interface InformService {
    int addInform(List<Inform> list);
    List<Inform> selectInform(Integer receiveId);
}
