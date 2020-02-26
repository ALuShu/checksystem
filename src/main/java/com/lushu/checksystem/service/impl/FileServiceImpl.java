package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.constant.BasicConstant;
import com.lushu.checksystem.constant.DatabaseConstant;
import com.lushu.checksystem.constant.OtherConstant;
import com.lushu.checksystem.dao.FileDao;
import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.pojo.PageBean;
import com.lushu.checksystem.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 */
@Service
@Slf4j
@SuppressWarnings("unchecked")/*防止maven打包时警告*/
public class FileServiceImpl implements FileService {

    @Value("${checksystem.root}")
    private String root;
    private FileDao fileDao;
    public FileServiceImpl(FileDao fileDao) {
        this.fileDao = fileDao;
    }

    @Override
    public PageBean<File> selectOldSubmitted(Integer submitter, int page, int limit) {
        HashMap<String, Object> pageMap = new HashMap<>(3);
        PageBean<File> pageBean = new PageBean<>();
        pageBean.setCurrentPage(page);
        pageBean.setPageSize(limit);
        int count = fileDao.countBySubmitter(submitter);
        pageBean.setTotalRecord(count);
        pageMap.put("start", (page-1) * limit);
        pageMap.put("limit", pageBean.getPageSize());
        pageMap.put("submitter", submitter);
        List<File> files = fileDao.selectFileBySubmitter(pageMap);
        pageBean.setList(files);
        return pageBean;
    }

    @Override
    public PageBean<Map<String, Object>> showFileList(String path, int page, int limit) {
        List<Map<String, Object>> fileList = new ArrayList<>();
        try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(root+path))) {
            for (Path pathObj : directoryStream) {
                BasicFileAttributes attrs = Files.readAttributes(pathObj, BasicFileAttributes.class);
                Map<String, Object> fileItem = new HashMap<>(4);
                fileItem.put("name", pathObj.getFileName().toString());
                fileItem.put("date", OtherConstant.DATE_FORMAT.format(new Date(attrs.lastModifiedTime().toMillis())));
                fileItem.put("size", attrs.size());
                fileItem.put("type", attrs.isDirectory() ? "dir" : "file");
                fileList.add(fileItem);
            }
            int start = (page-1)*limit;
            int end = Math.min((start + limit), fileList.size());
            List<Map<String, Object>> newFileList = fileList.subList(start, end);
            PageBean<Map<String, Object>> pageBean = new PageBean<>();
            pageBean.setList(newFileList);
            pageBean.setTotalRecord(fileList.size());
            return pageBean;
        } catch (IOException e) {
            log.error("文件遍历失败", e);
            return null;
        }
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
    public int addFiles(Collection<MultipartFile> files, String path , Integer submitter) {
        Iterator<MultipartFile> fileIterator = files.iterator();
            Integer owner = Integer.parseInt(path.substring(0,4));
            if (files.size() == 1){
            File preFile = addMethod(fileIterator.next(), new File(), root+path, owner, submitter);
            File isExist = fileDao.checkFile(preFile);
            if (isExist == null){
                return (preFile!=null) ? fileDao.addFile(preFile) : -1;
            }else {
                return (preFile!=null) ? 0 : -1;
            }

        }else {
            List<File> preFiles = new ArrayList<>();
            while (fileIterator.hasNext()){
                File current = addMethod(fileIterator.next(), new File(), root+path, owner, submitter);
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
        if (fileName != null) {
            java.io.File dest = new java.io.File(path, fileName);
            /*判断是file还是dir*/
            if (dest.getName().contains(".")) {
                if (dest.exists()) {
                    log.info("添加覆盖操作");
                    boolean res = dest.delete();
                    if (!res) {
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
                    log.error("添加保存失败", e);
                    return null;
                }
            } else {
                destFile.setType(DatabaseConstant.File.DIRECTORY_FILE.getFlag());
                destFile.setPermission("-rwx-rwx-rw-");
                if (dest.exists()) {
                    log.info("添加覆盖操作");
                    boolean res = dest.delete();
                    if (!res) {
                        log.error("覆盖失败，请检查文件夹是否非空");
                        return null;
                    }
                }
                try {
                    Files.createDirectory(Paths.get(path, fileName));
                } catch (IOException e) {
                    log.error("创建新文件夹失败", e);
                    return null;
                }
            }
        }
        destFile.setStatus(DatabaseConstant.File.UNCHECKED.getFlag());
        destFile.setName(fileName);
        destFile.setPath(path);
        destFile.setSize(paramFile.getSize());
        destFile.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date()));
        destFile.setOwner(owner);
        return destFile;
    }

    @Override
    public int updateFiles(HashMap<String, Object> updateParam, String action){
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
            if (updateParam.get("resource") instanceof ArrayList<?>){
                List<Path> resources = (ArrayList<Path>) updateParam.get("resource");
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
            if (updateParam.get("resource") instanceof ArrayList<?>){
                List<File> files = new ArrayList<>();
                List<Path> resources = (ArrayList<Path>) updateParam.get("resource");
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
        return 0;
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
    public int deleteFile(HashMap<String, Object> param) {
        //dir or file;null dir;name and path
        String path = (String)param.get("path");
        int delRes = 0;
        if (param.get("fileName") instanceof ArrayList<?>){
            List<String> nameList = (ArrayList<String>) param.get("fileName");
            for (String fileName : nameList){
                File current = deleteMethod(path, fileName);
                if (current != null) {
                    delRes += fileDao.deleteFile(current);
                }
            }
        }else {
            String fileName = (String) param.get("fileName");
            File current = deleteMethod(path, fileName);
            delRes = fileDao.deleteFile(current);
        }
        return delRes;
    }

    /**
     * 删除文件
     */
    private static File deleteMethod(String path, String fileName){
        Path current = Paths.get(path, fileName);
        File file = new File();
        try {
            Files.delete(current);
        }catch (IOException e){
            log.error("删除文件"+fileName+"失败", e);
            return null;
        }
        file.setName(fileName);
        file.setPath(path);
        return file;
    }

    @Override
    public int deleteTeacherFiles(Integer owner) {
        return fileDao.deleteFileByOwner(owner);
    }

    @Override
    public int deleteUnPassedFiles(Integer submitter, Integer status) {
        return fileDao.deleteFilesByStatus(submitter, status);
    }
}
