package com.lushu.checksystem;

import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.Student;
import com.lushu.checksystem.pojo.Teacher;
import com.lushu.checksystem.service.AdminService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lushu
 * @date 19-11-11 下午10:08
 **/
class AdminTest extends ChecksystemApplicationTests{

    @Autowired
    private AdminService adminService;

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
        a.setPassword("111111");
        a.setDepartment("计算机");
        a.setMajor("Web应用软件开发");
        students.add(a);
        b.setId(BigInteger.valueOf(1640706200));
        b.setName("阿鲁");
        b.setPassword("111111");
        b.setDepartment("计算机");
        b.setMajor("Web应用软件开发");
        students.add(b);
        c.setId(BigInteger.valueOf(1640706199));
        c.setName("鲁");
        c.setPassword("111111");
        c.setDepartment("计算机");
        c.setMajor("Web应用软件开发");
        students.add(c);
        assertEquals(3,adminService.addStudents(students));
    }

    @Test
    void PoiTest() {
        File file = new File("D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\excel\\test.xlsx");
        try {
            FileInputStream in = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(in);//workbook对象
            for (int i = 0; i < workbook.getNumberOfSheets(); i++){
                XSSFSheet sheet = workbook.getSheetAt(i);//表对象
                for (int row = 0; row <= sheet.getLastRowNum(); row++){
                    XSSFRow sheetRow = sheet.getRow(row);//行对象
                    if (sheetRow != null){
                        for (int cell = 0; cell < sheetRow.getLastCellNum(); cell++){
                            XSSFCell xssfCell = sheetRow.getCell(cell);//纯数字过大时会成科学计数法
                            xssfCell.setCellType(CellType.STRING);
                            System.out.println(xssfCell.toString());
                        }
                    }
                }
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteStudentsTest(){
        integerList.add(BigInteger.valueOf(1640706201));
        integerList.add(BigInteger.valueOf(1640706200));
        integerList.add(BigInteger.valueOf(1640706199));
        assertEquals(3,adminService.deleteStudents(integerList));
    }

    @Test
    void addTeachersTest(){
        ta.setId(BigInteger.valueOf(8080));
        ta.setName("小刘");
        ta.setPassword("000000");
        ta.setDepartment("计算机");
        teachers.add(ta);
        tb.setId(BigInteger.valueOf(9090));
        tb.setName("小红");
        tb.setPassword("000000");
        tb.setDepartment("计算机");
        teachers.add(tb);
        tc.setId(BigInteger.valueOf(6060));
        tc.setName("小王");
        tc.setPassword("000000");
        tc.setDepartment("计算机");
        teachers.add(tc);
        assertEquals(3, adminService.addTeachers(teachers));
    }

    @Test
    void deleteTeachersTest(){
        integerList.add(BigInteger.valueOf(8080));
        integerList.add(BigInteger.valueOf(9090));
        integerList.add(BigInteger.valueOf(6060));
        assertEquals(3,adminService.deleteTeachers(integerList));
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

    @Test
    void selectTeacherTest(){
    }

    @Test
    void selectStudentTest(){
    }
}
