package com.supermall.user.web;

import com.supermall.user.pojo.User;
import com.supermall.user.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    UserService userService;
    @org.junit.Test
    public void checkData() {
        User jimxue = userService.queryUserByUsernameAndPassword("supermall", "1234");
        System.out.println(jimxue);

    }
}