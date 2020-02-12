package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.constant.BasicConstant;
import com.lushu.checksystem.constant.DatabaseConstant;
import com.lushu.checksystem.constant.OtherConstant;
import com.lushu.checksystem.dao.FileDao;
import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
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
        teacherFile.setType(DatabaseConstant.File.DIRECTORY_FILE.getFlag());
        teacherFile.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date()));
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
            return (preFile!=null) ? fileDao.addFile(preFile) : -1;
        }else {
            List<File> preFiles = new ArrayList<>();
            while (fileIterator.hasNext()){
                File current = addMethod(fileIterator.next(), new File(), path, owner, submitter);
                if (current == null){
                    preFiles.clear();
                    break;
                }
                preFiles.add(current);
            }
            return (preFiles.size()!=0) ? fileDao.addFiles(preFiles) : -1;
        }
    }

    /**
     * 在磁盘添加文件或文件夹，并生成一个File对象
     */
    private static File addMethod(MultipartFile paramFile, File destFile, String path, Integer owner, Integer submitter) {
        String fileName = paramFile.getOriginalFilename();
        java.io.File dest = new java.io.File(path, fileName);
        if (dest.isFile()){
            if (dest.exists()){
                log.info("添加覆盖操作");
                boolean res = dest.delete();
                if (!res){
                    log.error("覆盖失败，请检查文件夹是否非空");
                    return null;
                }
            }
            destFile.setType(DatabaseConstant.File.WORD_FILE.getFlag());
            destFile.setPermission("-rwx-rwx-r-x");
            destFile.setSubmitter(submitter);
            try {
                paramFile.transferTo(dest);
            } catch (IOException e) {
                log.error("添加保存失败",e);
                return null;
            }
        }else {
            destFile.setType(DatabaseConstant.File.DIRECTORY_FILE.getFlag());
            destFile.setPermission("-rwx-rwx-rw-");
            if (dest.exists()){
                log.info("添加覆盖操作");
                boolean res = dest.delete();
                if (!res){
                    log.error("覆盖失败，请检查文件夹是否非空");
                    return null;
                }
            }
            try {
                Files.createDirectory(Paths.get(path, fileName));
            } catch (IOException e) {
                log.error("创建新文件夹失败",e);
                return null;
            }
        }
        destFile.setName(fileName);
        destFile.setPath(path);
        destFile.setSize(paramFile.getSize());
        destFile.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date()));
        destFile.setOwner(owner);
        return destFile;
    }

    @Override
    public Integer updateFiles(HashMap<String, Object> updateParam, String action){
        if (BasicConstant.FileAction.RENAME.getString().equals(action)){
            Path resource = (Path) updateParam.get("resource");
            String newName = (String) updateParam.get("newName");
            try {
                if (Files.exists(resource)) {
                    Files.move(resource, resource.resolveSibling(newName));
                    File newFile = new File();
                    newFile.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date()));
                    newFile.setName(newName);
                    return fileDao.updateFile(newFile);
                }
            } catch (IOException e) {
                log.error("重命名失败",e);
                return -1;
            }

        }else if (BasicConstant.FileAction.MOVE.getString().equals(action)){
            Path newDir = (Path) updateParam.get("newDir");
            if (updateParam.get("resource") instanceof List<?>){
                List<Path> resources = (List<Path>) updateParam.get("resource");
                List<File> files = new ArrayList<>();
                for (Path current : resources){
                    String fileName = current.getFileName().toString();
                    File newObject = moveMethod(current, newDir, fileName);
                    if (newObject == null){
                        files.clear();
                        break;
                    }
                    files.add(newObject);
                }
                return (files.size() != 0) ? fileDao.updateFiles(files) : -1;
            }else {
                Path resource = (Path) updateParam.get("resource");
                String fileName = resource.getFileName().toString();
                File newObject = moveMethod(resource, newDir, fileName);
                return (newObject != null) ? fileDao.updateFile(newObject) : -1;
            }

        }else if (BasicConstant.FileAction.CORRECT.getString().equals(action)){
            Integer status = (Integer) updateParam.get("status");
            long time = System.currentTimeMillis();
            FileTime fileTime = FileTime.fromMillis(time);
            if (updateParam.get("resource") instanceof List<?>){
                List<File> files = new ArrayList<>();
                List<Path> resources = (List<Path>) updateParam.get("resource");
                for (Path resource : resources){
                    try {
                        Files.setAttribute(resource, "basic:lastModifiedTime", fileTime);
                    }catch (IOException e){
                        log.error("修改更新时间失败",e);
                        files.clear();
                        break;
                    }
                    File file = new File();
                    file.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date(fileTime.toMillis())));
                    file.setStatus(status);
                    files.add(file);
                }
                return (files.size() != 0) ? fileDao.updateFiles(files) : -1;
            }else {
                Path resource = (Path) updateParam.get("resource");
                try {
                    Files.setAttribute(resource, "basic:lastModifiedTime", fileTime);
                } catch (IOException e) {
                    e.printStackTrace();
                    return -1;
                }
                File file = new File();
                file.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date(fileTime.toMillis())));
                file.setStatus(status);
                return fileDao.updateFile(file);
            }
        }
        return null;
    }

    /**
     * 判断是移动还是移动覆盖操作，并生成File对象
     */
    private static File moveMethod(Path source, Path newDir, String filename){
        File file = new File();
        //覆盖
        if (Files.exists(Paths.get(newDir.toString(), filename))){
            try {
                //size
                file.setSize((Long) Files.getAttribute(source, "basic:size"));
            } catch (IOException e) {
                log.error("覆盖操作获取文件大小失败",e);
                return file;
            }
        }
        try {
            if (Files.exists(source)){
                Path current = Files.move(source, newDir, StandardCopyOption.REPLACE_EXISTING);
                //path，updateTime
                file.setPath(newDir.getParent().toString());
                FileTime ft = (FileTime) Files.getAttribute(current, "basic:lastModifiedTime");
                file.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date(ft.toMillis())));
            }
        }catch (IOException e){
            log.error("移动失败",e);
            file = null;
        }
        return file;
    }


    @Override
    public Integer deleteFiles(HashMap<String, Object> param) throws IOException {
        return 0;
    }
}
