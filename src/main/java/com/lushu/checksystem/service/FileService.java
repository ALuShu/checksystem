package com.lushu.checksystem.service;

import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.pojo.PageBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 展示系统文件列表
     * @param path
     * @param page
     * @param limit
     * @return
     */
    PageBean<Map<String, Object>> showFileList(String path, int page, int limit);

    /**
     * 教师注册时，新建的教师文件夹
     */
    int newTeacherFile(String username, String realname, Integer id);

    /**
     * 创建文件或文件夹时，往数据库添加相应记录，支持批量操作
     * paramMap包含(Collection<MultipartFile> files, String path, Integer owner, Integer submitter)
     */
    int addFiles(Collection<MultipartFile> files, String path, Integer owner, Integer submitter);

    /**
     * 更新文件属性时，更新数据库相关记录，支持批量操作
     * 动态更新
     * 更新后的name文件名；path物理路径；size文件大小；updateTime修改时间；status文件状态
     */
    int updateFiles(HashMap<String, Object> updateParam, String action);

    /**
     * 删除文件
     */
    int deleteFile(HashMap<String, Object> param);

    /**
     * 删除教师及文件夹下所有文件
     */
    int deleteTeacherFiles(Integer owner);

    /**
     * 删除学生未通过的作业
     */
    int deleteUnPassedFiles(Integer submitter, Integer status);
}
