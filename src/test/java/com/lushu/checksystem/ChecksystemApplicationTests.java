package com.lushu.checksystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ChecksystemApplication.class)
class ChecksystemApplicationTests {

    @BeforeEach
    public void init() {
        System.out.println("开始测试-----------------");
    }

    @AfterEach
    public void after() {
        System.out.println("测试结束-----------------");
    }
}
