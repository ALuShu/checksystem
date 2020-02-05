package com.lushu.checksystem.dao;

import com.lushu.checksystem.pojo.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
public interface FileDao {

    /**
     * 查询同提交人的所有文件
     * @param submitter
     * @return
     */
    List<File> selectFileBySubmitter(Integer submitter);

    /**
     * 查询同拥有人的所有文件
     * @param owner
     * @return
     */
    List<File> selectFileByOwner(Integer owner);

    /**
     * 根据id群查询文件群
     * @param ids
     * @return
     */
    List<File> selectFileByIds(List<Integer> ids);

    /**
     * 增加文件
     * @param file
     * @return
     */
    Integer addFile(File file);

    /**
     * 更新文件,作业状态
     * @param file
     * @return
     */
    Integer updateFile(File file);

    /**
     * 删除文件
     * @param id
     * @return
     */
    Integer deleteFile(Integer id);

    /**
     * 批量增加文件
     * @param file
     * @return
     */
    Integer addFiles(List<File> file);

    /**
     * 批量更新文件
     * @param file
     * @return
     */
    Integer updateFiles(List<File> file);

    /**
     * 批量删除文件（根据owner）
     * @param owner
     * @return
     */
    Integer deleteFilesByOwner(List<Integer> owner);

    /**
     * 批量删除文件（根据owner和status）
     * @param owner
     * @param status
     * @return
     */
    Integer deleteFilesByStatus(@Param("owner")Integer owner, @Param("status")Integer status);
}
