package com.example.operlog.util;

import com.example.operlog.entity.User;

/**
 * @program: learn
 * @description: 模拟用户
 * @author: Mr.liu
 * @create: 2020-11-13 15:48
 **/
public class GetUserUtils {

    public static User getCurrentUser(){
        return new User("123","admin");
    }
}
