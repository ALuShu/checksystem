package com.lushu.checksystem.service;

import com.lushu.checksystem.pojo.File;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 * @throws
 * @since
 */
public interface FileService {

    int newTeacherFile(String username, String realname, Integer id);

    int addFile(File file);

    int updateFile(File file);

    int deleteFile(Integer id);
}
