package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.dao.StudentDao;
import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.Student;
import com.lushu.checksystem.pojo.Teacher;
import com.lushu.checksystem.service.StudentService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * @author lushu
 * @date 19-11-12 下午4:56
 **/
@Service
public class StudentServiceImpl implements StudentService {

    private StudentDao studentDao;
    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Student selStudent(Student student) {
        return studentDao.selStudent(student);
    }

    @Override
    public int addWorkFile(File file) {
        return studentDao.addWorkFile(file);
    }

    @Override
    public int updatePassword(Student student) {
        return studentDao.updatePassword(student);
    }

    @Override
    public List<File> selectWorkFile(BigInteger owner) {
        return studentDao.selectWorkFile(owner);
    }

    @Override
    public List<Inform> selectInform(BigInteger receiveId) {
        return studentDao.selectInform(receiveId);
    }

    @Override
    public List<Teacher> selectTeacher() {
        return studentDao.selectTeacher();
    }
}
