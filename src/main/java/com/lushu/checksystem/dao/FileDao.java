package com.lushu.checksystem.dao;

import com.lushu.checksystem.pojo.File;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author ALuShu
 * @date 2020/1/10
 * @throws
 * @since
 * @Description
 */
@Repository
@Mapper
public interface FileDao {

    /**
     * 查询学生作业
     * @param owner
     * @return
     */
    //List<File> selectWorkFile(Integer owner);

    /**
     * 查询目录,分页
     * @param id
     * @param owner
     * @param type
     * @return
     */
    //List<File> selectDirFile(Integer id, Integer owner, Integer type);

    /**
     * 上传作业
     * @param file
     * @return
     */
    int addWorkFile(File file);

    /**
     * 增加目录
     * @param file
     * @return
     */
    int addFile(File file);

    /**
     * 更新目录,作业状态
     * @param file
     * @return
     */
    int updateFile(File file);

    /**
     * 删除目录
     * @param id
     * @return
     */
    int deleteFile(Integer id);


}
