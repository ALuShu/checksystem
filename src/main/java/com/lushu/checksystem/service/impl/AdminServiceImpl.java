package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.dao.AdminDao;
import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.Student;
import com.lushu.checksystem.pojo.Teacher;
import com.lushu.checksystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * @author lushu
 * @date 19-11-12 下午4:43
 **/
@Service
public class AdminServiceImpl implements AdminService {

    private AdminDao adminDao;
    public AdminServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public int addStudents(List<Student> students) {
        return adminDao.addStudents(students);
    }

    @Override
    public int deleteStudents(List<BigInteger> list) {
        return adminDao.deleteStudents(list);
    }

    @Override
    public int addTeachers(List<Teacher> teachers) {
        return adminDao.addTeachers(teachers);
    }

    @Override
    public int deleteTeachers(List<BigInteger> list) {
        return adminDao.deleteTeachers(list);
    }

    @Override
    public int updateStudent(List<Student> students) {
        return adminDao.updateStudent(students);
    }

    @Override
    public int updateTeacher(List<Teacher> teachers) {
        return adminDao.updateTeacher(teachers);
    }

    @Override
    public int addInforms(List<Inform> informs) {
        return adminDao.addInforms(informs);
    }

    @Override
    public Teacher selectTeacher(Teacher teacher) {
        return adminDao.selectTeacher(teacher);
    }

    @Override
    public Student selectStudent(Student student) {
        return adminDao.selectStudent(student);
    }

    @Override
    public List<Student> selectStudents() {
        return adminDao.selectStudents();
    }
}
