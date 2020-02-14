package com.lushu.checksystem;

import com.lushu.checksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lushu
 * @date 19-11-11 下午10:08
 **/
@Slf4j
class UserTest extends ChecksystemApplicationTests{

    @Autowired
    private UserService userService;

    @Test
    public void test(){
        log.debug("用户总数："+userService.countUsers());
    }

}
