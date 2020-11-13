package com.example.operlog.entity;

import lombok.Data;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-13 15:48
 **/
@Data
public class User {
    private String userId;
    private String username;

    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
