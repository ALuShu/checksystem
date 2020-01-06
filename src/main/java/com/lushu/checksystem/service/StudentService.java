package com.lushu.checksystem.service;

import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.Student;
import com.lushu.checksystem.pojo.Teacher;

import java.math.BigInteger;
import java.util.List;

/**
 * @author lushu
 * @date 19-11-12 下午4:42
 **/

public interface StudentService {
    Student selStudent(Student student);
    int addWorkFile(File file);
    int updatePassword(Student student);
    List<File> selectWorkFile(BigInteger owner);
    List<Inform> selectInform(BigInteger receiveId);

    /**
     * 分页教师列表
     */
    List<Teacher> selectTeacher();
}
