package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.dao.TeacherDao;
import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.Teacher;
import com.lushu.checksystem.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * @author lushu
 * @date 19-11-12 下午4:56
 **/
@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherDao teacherDao;
    /**
     * 尽量避免使用Autowired,使用构造器注入强制依赖或setter注入选择依赖
     */
    public TeacherServiceImpl(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }


    @Override
    public int addFile(File file) {
        return teacherDao.addFile(file);
    }

    @Override
    public int updateFile(File file) {
        return teacherDao.updateFile(file);
    }

    @Override
    public int deleteFile(BigInteger id) {
        return teacherDao.deleteFile(id);
    }

    @Override
    public int updatePassword(Teacher teacher) {
        return teacherDao.updatePassword(teacher);
    }

    @Override
    public int addInform(List<Inform> list) {
        return teacherDao.addInform(list);
    }

    @Override
    public List<Inform> selectInform(BigInteger receiveId) {
        return teacherDao.selectInform(receiveId);
    }

    @Override
    public List<File> selectCourseFile(BigInteger id, BigInteger owner, Integer type) {
        return teacherDao.selectCourseFile(id, owner, type);
    }

    @Override
    public List<File> selectClassFile(BigInteger id, BigInteger owner, Integer type) {
        return teacherDao.selectClassFile(id, owner, type);
    }

    @Override
    public List<File> selectWorkFile(BigInteger id, BigInteger owner, Integer type) {
        return teacherDao.selectWorkFile(id, owner, type);
    }
}
