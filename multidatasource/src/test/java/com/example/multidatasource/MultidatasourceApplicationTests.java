package com.example.multidatasource;

import com.example.multidatasource.entity.User;
import com.example.multidatasource.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootTest
class MultidatasourceApplicationTests {

    @Autowired
    UserService userService;
    @Test
    void contextLoads() {
    }
    @Test
    public void testWrite() {

        userService.insertUser(new User(UUID.randomUUID().toString(),"XXX"));
    }

    @Test
    public void testRead() {
        for (int i = 0; i < 4; i++) {
            List<User> users = userService.selectAllUser();
            System.out.println(users.toString());
        }
    }

}
