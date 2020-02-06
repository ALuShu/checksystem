package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.dao.FileDao;
import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public List<File> selectOldSubmitted(Integer submitter) {
        List<File> oldFiles = fileDao.selectFileBySubmitter(submitter);
        return oldFiles;
    }

    @Override
    public Integer newTeacherFile(String username, String realname, Integer id) {
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
    public Integer addFiles(Collection<MultipartFile> files, String path) {
        Integer addRes = 0;
        Iterator<MultipartFile> fileIterator = files.iterator();

        if (files.size() == 1){
            MultipartFile file = fileIterator.next();
            File preFile = new File();

        }else {

            List<File> preFiles = new ArrayList<>();
        }
        return addRes;
    }

    @Override
    public Integer updateFiles(List<File> files) {
        return 0;
    }

    @Override
    public Integer deleteFiles(HashMap<String, Object> param) {
        return 0;
    }
}
