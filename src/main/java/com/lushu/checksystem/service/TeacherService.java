package com.lushu.checksystem.service;

import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.Teacher;

import java.math.BigInteger;
import java.util.List;

/**
 * @author lushu
 * @date 19-11-12 下午4:41
 **/

public interface TeacherService {
    Teacher selTeacher(Teacher teacher);
    int addFile(File file);
    int updateFile(File file);
    int deleteFile(BigInteger id);
    int updatePassword(Teacher teacher);
    int addInform(List<Inform> list);
    List<Inform> selectInform(BigInteger receiveId);

    /**
     * 课程目录，分页
     */
    List<File> selectCourseFile(BigInteger id, BigInteger owner, Integer type);

    /**
     * 班级目录，分页
     */
    List<File> selectClassFile(BigInteger id, BigInteger owner, Integer type);

    /**
     * 作业列表，分页
     */
    List<File> selectWorkFile(BigInteger id, BigInteger owner, Integer type);
}
