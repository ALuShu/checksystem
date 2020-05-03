package com.lushu.checksystem;

import com.lushu.checksystem.constant.DatabaseConstant;
import com.lushu.checksystem.constant.OtherConstant;
import com.lushu.checksystem.pojo.User;
import com.lushu.checksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/2/14
 * @throws
 * @since
 */
@Component
@Slf4j
public class initializationBean {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init(){
        List<User> root = new ArrayList<>(1);
        User user = new User();
        user.setUsername("root");
        root.add(user);
        userService.addUsersByExcel(root, DatabaseConstant.Role.ROLE_ADMIN.ordinal()+1);
        File programRoot = new File(OtherConstant.REALPATH);
        if (!programRoot.exists()){
            programRoot.mkdir();
        }
    }
}
