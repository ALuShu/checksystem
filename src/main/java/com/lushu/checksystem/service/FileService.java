package com.lushu.checksystem.service;

import com.lushu.checksystem.pojo.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
     * paramMap包含(Collection<MultipartFile> files, String path, Integer owner, Integer submitter)
     */
    Integer addFiles(Collection<MultipartFile> files, String path, Integer owner, Integer submitter) throws IOException;

    /**
     * 更新文件属性时，更新数据库相关记录，支持批量操作
     * 动态更新
     * 更新后的name文件名；path物理路径；size文件大小；updateTime修改时间；status文件状态
     */
    Integer updateFiles(HashMap<String, Object> updateParam);

    /**
     * 删除文件时，要删除对应记录，支持批量操作
     */
    Integer deleteFiles(HashMap<String, Object> param);
}
