package com.lushu.checksystem.dao;

import com.lushu.checksystem.pojo.File;
import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
/**
 * Teacher持久层接口
 * @author ALuShu
 * @date 2019/11/8
 */
@Repository
@Mapper
public interface TeacherDao {
    /**
     * 登录检查
     * @param teacher a param of Teacher
     * @return the correct Teacher
     */
    Teacher selTeacher(Teacher teacher);

    /**
     * 增加目录
     * @param file a param of File
     * @return the rows of influenced
     */
    int addFile(File file);

    /**
     * 更新目录,作业状态
     * @param file a param of File
     * @return the rows of influenced
     */
    int updateFile(File file);

    /**
     * 删除目录
     * @param id a param of BigInteger
     * @return the rows of influenced
     */
    int deleteFile(BigInteger id);

    /**
     * 更新密码
     * @param teacher a param of teacher object
     * @return the rows of influenced
     */
    int updatePassword(Teacher teacher);

    /**
     * 批量发布通知
     * @param list a param of List
     * @return the rows of influenced
     */
    int addInform(List<Inform> list);

    /**
     * 查询通知
     * @param receiveId a param of BigInteger
     * @return a List of Inform
     */
    List<Inform> selectInform(BigInteger receiveId);

    /**
     * 查询课程目录,分页
     * @param id a param of BigInteger
     * @param owner a param of BigInteger
     * @param type a param of Integer
     * @return a List of File
     */
    List<File> selectCourseFile(BigInteger id, BigInteger owner, Integer type);

    /**
     * 查询班级目录,分页
     * @param id a param of BigInteger
     * @param owner a param of BigInteger
     * @param type a param of Integer
     * @return a List of File
     */
    List<File> selectClassFile(BigInteger id, BigInteger owner, Integer type);

    /**
     * 查询作业目录,分页 or 作业查重
     * @param id a param of BigInteger
     * @param owner a param of BigInteger
     * @param type a param of Integer
     * @return a List of File
     */
    List<File> selectWorkFile(BigInteger id, BigInteger owner, Integer type);

}
