package com.lushu.checksystem.service;

import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.Student;
import com.lushu.checksystem.pojo.Teacher;

import java.math.BigInteger;
import java.util.List;

/**
 * @author lushu
 * @date 19-11-12 下午4:40
 **/

public interface AdminService {
    int addStudents(List<Student> students);
    int deleteStudents(List<BigInteger> list);
    int addTeachers(List<Teacher> teachers);
    int deleteTeachers(List<BigInteger> list);
    int updateStudent(List<Student> students);
    int updateTeacher(List<Teacher> teachers);
    int addInforms(List<Inform> informs);
    Teacher selectTeacher(Teacher teacher);
    Student selectStudent(Student student);
}
