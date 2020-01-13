package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.dao.FileDao;
import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.service.FileService;
import org.springframework.stereotype.Service;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 * @throws
 * @since
 */
@Service
public class FileServiceImpl implements FileService {

    private FileDao fileDao;
    public FileServiceImpl(FileDao fileDao) {
        this.fileDao = fileDao;
    }

    @Override
    public int addFile(File file) {
        return fileDao.addFile(file);
    }

    @Override
    public int updateFile(File file) {
        return fileDao.updateFile(file);
    }

    @Override
    public int deleteFile(Integer id) {
        return fileDao.deleteFile(id);
    }
}
