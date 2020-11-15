package com.example.multidatasource.entity;

import lombok.Data;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-16 01:27
 **/
@Data
public class User {
    private String id;
    private String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
