package com.lushu.checksystem;

import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.Student;
import com.lushu.checksystem.pojo.Teacher;
import com.lushu.checksystem.service.AdminService;
import com.lushu.checksystem.service.StudentService;
import com.lushu.checksystem.util.ExcelUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author lushu
 * @date 19-11-11 下午10:08
 **/
class AdminTest extends ChecksystemApplicationTests{

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;

    private Student a = new Student();
    private Student b = new Student();
    private Student c = new Student();
    private Teacher ta = new Teacher();
    private Teacher tb = new Teacher();
    private Teacher tc = new Teacher();
    private Inform ia = new Inform();
    private Inform ib = new Inform();
    private Inform ic = new Inform();
    private List<Student> students = new ArrayList<>();
    private List<Teacher> teachers = new ArrayList<>();
    private List<Inform> informs = new ArrayList<>();
    private List<BigInteger> integerList = new ArrayList<>();

    @Test
    void addStudentsTest(){
        //重复主键抛org.springframework.dao.DuplicateKeyException
        a.setId(BigInteger.valueOf(1640706201));
        //字符过长抛org.springframework.dao.DataIntegrityViolationException
        a.setName("阿鲁叔");
        a.setDepartment("计算机");
        a.setMajor("Web应用软件开发");
        students.add(a);
        b.setId(BigInteger.valueOf(1640706200));
        b.setName("阿鲁");
        b.setDepartment("计算机");
        b.setMajor("Web应用软件开发");
        students.add(b);
        c.setId(BigInteger.valueOf(1640706199));
        c.setName("鲁");
        c.setDepartment("计算机");
        c.setMajor("Web应用软件开发");
        students.add(c);
        assertEquals(3,adminService.addStudents(students));
    }

    @Test
    void PoiTest() {
        String fileName = "D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\excel\\test.xlsx";
        String fileName1 = "D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\excel\\testA.xls";
        ExcelUtil<Student> excelUtil = new ExcelUtil<>(Student.class);
        ExcelUtil<Teacher> excelUtil1 = new ExcelUtil<>(Teacher.class);
        try {
            List<Student> students = excelUtil.explain(fileName);
            List<Teacher> teachers = excelUtil1.explain(fileName1);
            for(Student s : students){
                System.out.println(s.toString());
            }
            for(Teacher s : teachers){
                System.out.println(s.toString());
            }
            assertEquals(students.size(), adminService.addStudents(students));
            assertEquals(teachers.size(), adminService.addTeachers(teachers));
        } catch (IOException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteStudentsTest(){
        students = adminService.selectStudents();
        for (Student s : students){
            System.out.println(s.toString());
        }
        for (int i = 0;i<students.size(); i++){
            integerList.add(students.get(i).getId());
        }
        assertEquals(students.size(),adminService.deleteStudents(integerList));
    }

    @Test
    void addTeachersTest(){
        ta.setId(BigInteger.valueOf(8080));
        ta.setName("小刘");
        ta.setDepartment("计算机");
        teachers.add(ta);
        tb.setId(BigInteger.valueOf(9090));
        tb.setName("小红");
        tb.setDepartment("计算机");
        teachers.add(tb);
        tc.setId(BigInteger.valueOf(6060));
        tc.setName("小王");
        tc.setDepartment("计算机");
        teachers.add(tc);
        assertEquals(3, adminService.addTeachers(teachers));
    }

    @Test
    void deleteTeachersTest(){
        teachers = studentService.selectTeacher();
        for(Teacher t:teachers){
            System.out.println(t.toString());
        }
        for (int i=0; i<teachers.size();i++){
            integerList.add(teachers.get(i).getId());
        }
        assertEquals(teachers.size(),adminService.deleteTeachers(integerList));
    }

    @Test
    void updateStudentTest(){
        a.setId(BigInteger.valueOf(1640706201));
        a.setName("阿鲁姨");
        a.setDepartment("物联网");
        a.setMajor("物联网应用开发");
        students.add(a);
        b.setId(BigInteger.valueOf(1640706200));
        b.setName("鲁");
        b.setMajor("物联网应用开发");
        students.add(b);
        c.setId(BigInteger.valueOf(1640706199));
        c.setDepartment("物联网");
        students.add(c);
        assertEquals(3, adminService.updateStudent(students));
    }

    @Test
    void updateTeacherTest(){
        ta.setId(BigInteger.valueOf(8080));
        ta.setName("小狗");
        ta.setDepartment("软件");
        teachers.add(ta);
        tb.setId(BigInteger.valueOf(9090));
        tb.setDepartment("外语");
        teachers.add(tb);
        tc.setId(BigInteger.valueOf(6060));
        tc.setName("小刘");
        teachers.add(tc);
        assertEquals(3, adminService.updateTeacher(teachers));
    }

    @Test
    void addInformsTest(){
        ia.setSendId(BigInteger.valueOf(9090));
        ia.setReceiveId(BigInteger.valueOf(1640706201));
        ia.setContent("今晚老地方见！");
        informs.add(ia);
        ib.setSendId(BigInteger.valueOf(8080));
        ib.setReceiveId(BigInteger.valueOf(1640706201));
        ib.setContent("今晚老地方见！");
        informs.add(ib);
        ic.setSendId(BigInteger.valueOf(6060));
        ic.setReceiveId(BigInteger.valueOf(1640706201));
        ic.setContent("今晚老地方见！");
        informs.add(ic);
        assertEquals(3, adminService.addInforms(informs));
    }

}
