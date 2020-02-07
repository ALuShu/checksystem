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
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
    public Integer addFiles(Collection<MultipartFile> files, String path, Integer owner, Integer submitter) throws IOException {
        Iterator<MultipartFile> fileIterator = files.iterator();
        if (files.size() == 1){

            File preFile = addMethod(fileIterator.next(), new File(), path, owner, submitter);
            return fileDao.addFile(preFile);

        }else {

            List<File> preFiles = new ArrayList<>();
            while (fileIterator.hasNext()){
                preFiles.add(addMethod(fileIterator.next(), new File(), path, owner, submitter));
            }
            return fileDao.addFiles(preFiles);

        }
    }

    /**
     * 在磁盘添加文件或文件夹，然后生成一个File对象
     */
    private static File addMethod(MultipartFile paramFile, File destFile, String path, Integer owner, Integer submitter) throws IOException {
        String fileName = paramFile.getOriginalFilename();
        destFile.setName(fileName);
        destFile.setPath(path);
        destFile.setSize(paramFile.getSize());
        destFile.setUpdateTime(dateFormat.format(new Date()));
        destFile.setOwner(owner);
        java.io.File dest = new java.io.File(path, fileName);
        if (dest.isFile()){
            destFile.setType(1);
            destFile.setPermission("-rwx-rwx-r-x");
            destFile.setSubmitter(submitter);
            if (dest.exists()){
                log.info("覆盖操作");
                dest.delete();
            }
            paramFile.transferTo(dest);
        }else {
            destFile.setType(0);
            destFile.setPermission("-rwx-rwx-rw-");
            if (dest.exists()){
                log.info("覆盖操作");
                dest.delete();
            }
            Files.createDirectory(Paths.get(path, fileName));
        }
        return destFile;
    }

    @Override
    public Integer updateFiles(HashMap<String, Object> updateParam) {
        //name必可选（重命名），path可选（移动），size可选（覆盖）

        return 0;
    }

    @Override
    public Integer deleteFiles(HashMap<String, Object> param) {
        return 0;
    }
}
