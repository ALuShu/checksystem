package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.dao.FileDao;
import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 * @throws
 * @since
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${checksystem.root}")
    private String root;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private FileDao fileDao;
    public FileServiceImpl(FileDao fileDao) {
        this.fileDao = fileDao;
    }

    @Override
    public int newTeacherFile(String username, String realname, Integer id) {
        File teacherFile = new File();
        /*教师文件夹名字由工号+姓名组成*/
        String teacherRoot = username +"_"+ realname;
        teacherFile.setName(teacherRoot);
        teacherFile.setOwner(id);
        teacherFile.setPath(root);
        teacherFile.setPermission("-rw--rwx-rwx");
        teacherFile.setType(0);
        teacherFile.setUpdateTime(dateFormat.format(new Date()));
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(root, "/"))) {
            for(Path pathObject : directoryStream){
                if (pathObject.getFileName().toString().equals(teacherRoot)){
                    log.info("已有同名文件夹:"+pathObject.getFileName().toString());
                    return 0;
                }
            }
            Files.createDirectory(Paths.get(root+teacherRoot));
            return fileDao.addFile(teacherFile);
        }catch (IOException e){
            log.error("教师:"+username+"创建文件夹失败!", e);
        }
        return -1;
    }

    @Override
    public int addFile(File file) {
        file.setUpdateTime(dateFormat.format(new Date()));
        file.setType(1);
        file.setPermission("-r---rwx-rwx");
        //file.setOwner();
        file.setOwner(1);
        file.setStatus(0);
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
