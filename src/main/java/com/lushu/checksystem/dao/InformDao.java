package com.lushu.checksystem.dao;

import com.lushu.checksystem.pojo.Inform;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ALuShu
 * @date 2020/1/10
 * @throws
 * @since
 * @Description
 */
@Repository
@Mapper
public interface InformDao {

    /**
     * 批量发布通知
     * @param list
     * @return
     */
    int addInform(List<Inform> list);

    /**
     * 查询通知
     * @param receiveId
     * @return
     */
    List<Inform> selectInform(Integer receiveId);
}
