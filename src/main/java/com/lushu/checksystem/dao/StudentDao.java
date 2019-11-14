package com.lushu.checksystem.dao;

import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.Student;
import com.lushu.checksystem.pojo.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
/**
 * Student持久层接口
 * @author ALuShu
 * @date 2019/11/8
 */
@Repository
@Mapper
public interface StudentDao {
    /**
     * 上传作业
     * @param file a param of File
     * @return the rows of influenced
     */
    int addWorkFile(File file);

    /**
     * 修改密码
     * @param student a filed of Student
     * @return the rows of influenced
     */
    int updatePassword(Student student);

    /**
     * 查询学生作业
     * @param owner a param of BigInteger
     * @return a List of File
     */
    List<File> selectWorkFile(BigInteger owner);

    /**
     * 查询通知
     * @param receiveId a param of BigInteger
     * @return a List of Inform
     */
    List<Inform> selectInform(BigInteger receiveId);

    /**
     * 教师列表,分页
     * @return a List of Teacher
     */
    List<Teacher> selectTeacher();

}
