package com.lushu.checksystem.service;

import com.lushu.checksystem.pojo.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 * @throws
 * @since
 */
public interface FileService {
    /**
     * 查询学生以往作业
     */
    List<File> selectOldSubmitted(Integer submitter);

    /**
     * 教师注册时，新建的教师文件夹
     */
    Integer newTeacherFile(String username, String realname, Integer id);

    /**
     * 创建文件或文件夹时，往数据库添加相应记录，支持批量操作
     */
    Integer addFiles(Collection<MultipartFile> files, String path);

    /**
     * 更新文件属性时，更新数据库相关记录，支持批量操作
     */
    Integer updateFiles(List<File> files);

    /**
     * 删除文件时，要删除对应记录，支持批量操作
     */
    Integer deleteFiles(HashMap<String, Object> param);
}
