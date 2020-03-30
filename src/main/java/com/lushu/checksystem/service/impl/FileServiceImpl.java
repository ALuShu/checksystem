package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.constant.BasicConstant;
import com.lushu.checksystem.constant.DatabaseConstant;
import com.lushu.checksystem.constant.OtherConstant;
import com.lushu.checksystem.dao.FileDao;
import com.lushu.checksystem.dao.UserDao;
import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.pojo.HaiMingDistance;
import com.lushu.checksystem.pojo.PageBean;
import com.lushu.checksystem.service.FileService;
import com.lushu.checksystem.util.SimHash;
import com.lushu.checksystem.util.WordUtil;
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
    private static UserDao userDao;

    public FileServiceImpl(FileDao fileDao, UserDao userDao) {
        this.fileDao = fileDao;
        FileServiceImpl.userDao = userDao;
    }

    @Override
    public PageBean<File> selectOldSubmitted(Integer submitter, int page, int limit) {
        HashMap<String, Object> pageMap = new HashMap<>(3);
        PageBean<File> pageBean = new PageBean<>();
        pageBean.setCurrentPage(page);
        pageBean.setPageSize(limit);
        Integer count = fileDao.countBySubmitter(submitter);
        pageBean.setTotalRecord(count);
        pageMap.put("start", (page - 1) * limit);
        pageMap.put("limit", pageBean.getPageSize());
        pageMap.put("submitter", submitter);
        List<File> files = fileDao.selectFileBySubmitter(pageMap);
        pageBean.setList(files);
        return pageBean;
    }

    @Override
    public PageBean<File> selectRecent(Integer owner, int page, int limit, String modern) {
        HashMap<String, Object> pageMap = new HashMap<>(3);
        PageBean<File> pageBean = new PageBean<>();
        pageBean.setCurrentPage(page);
        pageBean.setPageSize(limit);
        Integer count = fileDao.countByOwner(owner);
        pageBean.setTotalRecord(count);
        pageMap.put("start", (page - 1) * limit);
        pageMap.put("limit", pageBean.getPageSize());
        pageMap.put("owner", owner);
        if (modern != null) {
            pageMap.put("modern", modern);
        }
        List<File> files = fileDao.selectFileByOwner(pageMap);
        pageBean.setList(files);
        return pageBean;
    }

    @Override
    public PageBean<Map<String, Object>> showFileList(String path, int page, int limit) {
        List<Map<String, Object>> fileList = new ArrayList<>();
        //利用nio包的文件相关接口，此处生成一个对应目录下所有文件的Path集合，在for循环中遍历至自定义的结果集PageBean<T>中
        //PageBean<T>是分页实体类，包装了总页数、总记录数和数据集合等信息
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(root + path))) {
            //此处的File为自定义实体类，须和io下的File区分，用于查询数据库中，同一目录下所有File记录
            List<File> files = fileDao.checkFiles(root + path);
            for (Path pathObj : directoryStream) {
                //遍历每个Path对象，同readAttributes()读取基础属性，存进单个Map中
                BasicFileAttributes attrs = Files.readAttributes(pathObj, BasicFileAttributes.class);
                Map<String, Object> fileItem = new HashMap<>(4);
                fileItem.put("name", pathObj.getFileName().toString());
                fileItem.put("date", OtherConstant.DATE_FORMAT.format(new Date(attrs.lastModifiedTime().toMillis())));
                fileItem.put("size", attrs.size());
                fileItem.put("type", attrs.isDirectory() ? "dir" : "file");
                //读取对应数据库中文件记录的status字段，即文件查阅状态
                for (File file : files) {
                    if (file.getName().equals(pathObj.getFileName().toString())) {
                        fileItem.put("status", file.getStatus());
                        break;
                    }
                }
                fileList.add(fileItem);
            }
            int start = (page - 1) * limit;
            int end = Math.min((start + limit), fileList.size());
            List<Map<String, Object>> newFileList = fileList.subList(start, end);
            PageBean<Map<String, Object>> pageBean = new PageBean<>();
            pageBean.setList(newFileList);
            pageBean.setTotalRecord(fileList.size());
            pageBean.setPageSize(limit);
            pageBean.setCurrentPage(page);
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
        String teacherRoot = username + "_" + realname;
        teacherFile.setName(teacherRoot);
        teacherFile.setOwner(id);
        teacherFile.setPath(root);
        teacherFile.setPermission("-rw--rwx-rwx");
        teacherFile.setType(DatabaseConstant.File.DIRECTORY_FILE.getFlag());
        teacherFile.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date()));
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(root, "/"))) {
            for (Path pathObject : directoryStream) {
                if (pathObject.getFileName().toString().equals(teacherRoot)) {
                    log.info("已有同名文件夹:" + pathObject.getFileName().toString());
                    return 0;
                }
            }
            Files.createDirectory(Paths.get(root + teacherRoot));
            return fileDao.addFile(teacherFile);
        } catch (IOException e) {
            log.error("教师:" + username + "创建文件夹失败!", e);
        }
        return -1;
    }

    @Override
    public int addFiles(Collection<MultipartFile> files, String path, Integer submitter) {
        Iterator<MultipartFile> fileIterator = files.iterator();
        String ownerUsername ;
        if("\\".equals(path) || "/".equals(path)){
            ownerUsername = "root";
            path = "";
        }else if (!path.matches("^[0-9]{4}_{1}[A-Za-z\u4e00-\u9fa5]{0,}$")){
            ownerUsername = "root";
        }else {
            ownerUsername = path.substring(0, 4);
        }
        if (files.size() == 1) {
            File preFile = addMethod(fileIterator.next(), new File(), root + path, ownerUsername, submitter);
            File isExist = fileDao.checkFile(preFile.getName(), preFile.getPath());
            if (isExist == null) {
                return (preFile != null) ? fileDao.addFile(preFile) : -1;
            } else {
                return (preFile != null) ? 0 : -1;
            }

        } else {
            List<File> preFiles = new ArrayList<>();
            while (fileIterator.hasNext()) {
                File current = addMethod(fileIterator.next(), new File(), root + path, ownerUsername, submitter);
                if (current == null) {
                    preFiles.clear();
                    break;
                }
                preFiles.add(current);
            }
            return (preFiles.size() != 0) ? fileDao.addFiles(preFiles) : -1;
        }
    }

    /**
     * 在磁盘添加文件或文件夹，并生成一个File对象
     */
    private static File addMethod(MultipartFile paramFile, File destFile, String path, String ownerUsername, Integer submitter) {
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
                destFile.setStatus(DatabaseConstant.File.UNCHECKED.getFlag());
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
        destFile.setName(fileName);
        destFile.setPath(path);
        destFile.setSize(paramFile.getSize());
        destFile.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date()));
        destFile.setOwner(userDao.selectUserByUsername(ownerUsername).getId());
        return destFile;
    }

    @Override
    public Integer addDirectory(String name, String path, Integer owner) {
        Path direction;
        File tempFile = new File();
        if("\\".equals(path) || "/".equals(path)){
            direction = Paths.get(root, name);
            tempFile.setPath(root);
        }else {
            direction = Paths.get(root + path, name);
            tempFile.setPath(root + path);
        }
        try {
            Files.createDirectory(direction);
        } catch (IOException e) {
            log.error("创建文件夹失败", e);
            return 0;
        }
        tempFile.setName(name);
        tempFile.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date()));
        tempFile.setOwner(owner);
        tempFile.setType(DatabaseConstant.File.DIRECTORY_FILE.getFlag());
        tempFile.setPermission("-rwx-rwx-rw-");
        return fileDao.addFile(tempFile);
    }

    @Override
    public int updateFiles(HashMap<String, Object> updateParam, String action) {
        if (BasicConstant.FileAction.RENAME.getString().equals(action)) {
            String path = (String) updateParam.get("resourcePath");
            String oldName = (String) updateParam.get("resourceName");
            Path resource = Paths.get(root, path + "\\" + oldName);
            String newName = (String) updateParam.get("newName");
            try {
                if (Files.exists(resource)) {
                    Map<String, Object> param = new HashMap<>();
                    Files.move(resource, resource.resolveSibling(newName));
                    param.put("name", newName);
                    param.put("updateTime", OtherConstant.DATE_FORMAT.format(new Date()));
                    param.put("resourcePath", root + path);
                    param.put("resourceName", oldName);
                    return fileDao.updateFile(param);
                }
            } catch (IOException e) {
                log.error("重命名失败", e);
                return -1;
            }

        }/*else if (BasicConstant.FileAction.MOVE.getString().equals(action)){
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

        }*//*else if (BasicConstant.FileAction.CORRECT.getString().equals(action)){
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
            }*/ else {
            long time = System.currentTimeMillis();
            FileTime fileTime = FileTime.fromMillis(time);
            String path = (String) updateParam.get("resourcePath");
            String name = (String) updateParam.get("resourceName");
            Path resource = Paths.get(root, path + name);
            Integer status = (Integer) updateParam.get("status");
            try {
                Files.setAttribute(resource, "basic:lastModifiedTime", fileTime);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
            Map<String, Object> param = new HashMap<>();
            param.put("status", status);
            param.put("updateTime", OtherConstant.DATE_FORMAT.format(new Date()));
            param.put("resourcePath", path);
            param.put("resourceName", name);
            return fileDao.updateFile(param);
        }
        return 0;
    }

    /**
     * 判断是移动还是移动覆盖操作，并生成File对象
     */
    private static File moveMethod(Path source, Path newDir, String filename) {
        File file = new File();
        //覆盖
        if (Files.exists(Paths.get(newDir.toString(), filename))) {
            try {
                //size
                file.setSize((Long) Files.getAttribute(source, "basic:size"));
            } catch (IOException e) {
                log.error("覆盖操作获取文件大小失败", e);
                return file;
            }
        }
        try {
            if (Files.exists(source)) {
                Path current = Files.move(source, newDir, StandardCopyOption.REPLACE_EXISTING);
                //path，updateTime
                file.setPath(newDir.getParent().toString());
                FileTime ft = (FileTime) Files.getAttribute(current, "basic:lastModifiedTime");
                file.setUpdateTime(OtherConstant.DATE_FORMAT.format(new Date(ft.toMillis())));
            }
        } catch (IOException e) {
            log.error("移动失败", e);
            file = null;
        }
        return file;
    }


    @Override
    public int deleteFile(HashMap<String, Object> param) {
        String path = (String) param.get("path");
        String[] name = (String[]) param.get("fileName");
        Integer delRes = 0;
        if ("\\".equals(path) || "/".equals(path)) {
            path = "";
        }
        for (int i = 0; i < name.length; i++) {
            File current = deleteMethod(root + path, name[i]);
            delRes += fileDao.deleteFile(current);
        }
        return delRes;
    }

    /**
     * 删除文件
     */
    private static File deleteMethod(String path, String fileName) {
        Path current = Paths.get(path, fileName);
        File file = new File();
        try {
            Files.delete(current);
        } catch (IOException e) {
            log.error("删除文件" + fileName + "失败", e);
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

    @Override
    public List<File> checkMethod(String[] names, String path) {
        ArrayList<File> files = new ArrayList<>();
        ArrayList<SimHash> hashes = new ArrayList<>();
        String pathParam = root + path;
        for (int i = 0; i < names.length; i++) {
            File tmpFile = fileDao.checkFile(names[i], pathParam);
            try {
                SimHash tmpHash = new SimHash(WordUtil.readWord(pathParam + "\\" + names[i]), 64);
                String sign = tmpHash.strSimHash;
                tmpFile.setSign(sign);
                tmpFile.setStatus(DatabaseConstant.File.CHECKED.getFlag());
                hashes.add(tmpHash);
                files.add(tmpFile);
            } catch (IOException e) {
                log.error("simHash出错,检查文件是否存在", e);
            }
        }
        //更新sign字段
        Integer updRes = fileDao.updateFiles(files);
        //存放海明距离
        for (int i = 0; i < files.size() - 1; i++) {
            List<HaiMingDistance> distanceList = new ArrayList<>();
            String currentSign = files.get(i).getSign().toString();
            for (int j = i + 1; j < files.size(); j++) {
                String compareSign = files.get(j).getSign().toString();
                int distance = hashes.get(i).getDistance(currentSign, compareSign);
                HaiMingDistance haiMingDistance = new HaiMingDistance();
                haiMingDistance.setFilename(files.get(j).getName());
                haiMingDistance.setDistance(distance);
                distanceList.add(haiMingDistance);
            }
            files.get(i).setDistances(distanceList);
        }
        //将海明距离平铺至每个File对象
        for (int i = files.size() - 1; i > 0; i--) {
            String currentFilename = files.get(i).getName();
            List<HaiMingDistance> currentDistanceList;
            if (i == files.size() - 1) {
                currentDistanceList = new ArrayList<>();
            } else {
                currentDistanceList = files.get(i).getDistances();
            }
            for (int j = i - 1; j > -1; j--) {
                String beforeFilename = files.get(j).getName();
                List<HaiMingDistance> beforeDistanceList = files.get(j).getDistances();
                Iterator<HaiMingDistance> iterator = beforeDistanceList.iterator();
                while (iterator.hasNext()) {
                    HaiMingDistance distance = iterator.next();
                    if (currentFilename.equals(distance.getFilename())) {
                        HaiMingDistance newHaiMingDistance = new HaiMingDistance();
                        newHaiMingDistance.setFilename(beforeFilename);
                        newHaiMingDistance.setDistance(distance.getDistance());
                        currentDistanceList.add(newHaiMingDistance);
                        break;
                    }
                }
            }
            if (i == files.size() - 1) {
                files.get(i).setDistances(currentDistanceList);
            }
        }
        return files;
    }
}
