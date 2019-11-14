package com.lushu.checksystem.dao;

import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.Student;
import com.lushu.checksystem.pojo.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Admin持久层接口
 * @author ALuShu
 * @date 2019/11/8
 */
@Repository
@Mapper
public interface AdminDao {
    /**
     * 批量导入学生信息
     * @param list a param of List
     * @return the rows of influenced
     */
    int addStudents(List<Student> list);

    /**
     * 批量删除学生信息
     * @param list a param of List about student`s id
     * @return the rows of influenced
     */
    int deleteStudents(List<BigInteger> list);

    /**
     * 批量导入教师信息
     * @param list a param of List
     * @return the rows of influenced
     */
    int addTeachers(List<Teacher> list);

    /**
     * 批量删除教师信息
     * @param list a param of List about teacher`s id
     * @return the rows of influenced
     */
    int deleteTeachers(List<BigInteger> list);

    /**
     * 批量更新学生信息
     * @param list a param of list about student
     * @return the rows of influenced
     */
    int updateStudent(List<Student> list);

    /**
     * 批量更新教师信息
     * @param list a param of list about teacher
     * @return the rows of influenced
     */
    int updateTeacher(List<Teacher> list);

    /**
     * 批量发布通知
     * @param list a param of List
     * @return the rows of influenced
     */
    int addInforms(List<Inform> list);

    /**
     * 查询某个教师信息
     * @param teacher a param of Teacher object
     * @return a Object of Teacher
     */
    Teacher selectTeacher(Teacher teacher);

    /**
     * 查询某个学生信息
     * @param student a param of Student object
     * @return a Object of Student
     */
    Student selectStudent(Student student);
}
