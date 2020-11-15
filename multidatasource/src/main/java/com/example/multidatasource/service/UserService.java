package com.example.multidatasource.service;

import com.example.multidatasource.dao.UserDao;
import com.example.multidatasource.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-16 01:20
 **/
@Service
public class UserService {

    @Autowired
    private UserDao userDao;


    public List<User> selectAllUser(){
       return userDao.selectAllUser();
    }

    public int  insertUser(User user){
        return userDao.insertUser(user);
    }

}
