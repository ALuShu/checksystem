package com.lushu.checksystem;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.lushu.checksystem.service.StudentService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.xzixi.algorithm.simhash.common.util.CommonUtils;
import com.xzixi.algorithm.simhash.common.AbstractKeywordsExtractor;
import com.xzixi.algorithm.simhash.common.Config;
import com.xzixi.algorithm.simhash.common.HashGenerator;
import com.xzixi.algorithm.simhash.common.KeywordsExtractor;
import com.xzixi.algorithm.simhash.common.SimHashException;

/**
 * @author lushu
 * @date 19-11-11 下午10:07
 **/
class StudentTest extends ChecksystemApplicationTests {

    @Autowired
    private StudentService studentService;

    @Test
    void addWorkFileTest(){

    }

    @Test
    void updatePasswordTest(){

    }

    @Test
    void selectWorkFileTest(){

    }

    @Test
    void selectInformTest(){

    }

    @Test
    void selectTeacherTest(){

    }
}
