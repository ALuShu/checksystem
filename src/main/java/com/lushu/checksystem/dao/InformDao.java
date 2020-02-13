package com.lushu.checksystem.dao;

import com.lushu.checksystem.pojo.Inform;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
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
     * 查询通知
     * @param sendId
     * @return
     */
    List<Inform> selectInforms(Integer sendId);

    /**
     * 查询所有通知，分页，按日期排序
     * @param param
     * @return
     */
    List<Inform> selectAllInforms(HashMap<String, Object> param);

    /**
     * 根据必修或选修查询通知
     * @param type
     * @return
     */
    List<Inform> selectInformsByType(boolean type);

    /**
     * 统计
     * @return
     */
    Integer count();

    /**
     * 批量发布通知
     * @param list
     * @return
     */
    Integer addInforms(List<Inform> list);

    /**
     * 修改通知
     * @param inform
     * @return
     */
    Integer updateInform(Inform inform);

    /**
     * 批量删除通知
     * @param ids
     * @return
     */
    Integer deleteInforms(List<Integer> ids);
}
