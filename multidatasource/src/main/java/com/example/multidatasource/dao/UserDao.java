package com.example.multidatasource.dao;

import com.example.multidatasource.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-16 01:26
 **/
@Mapper
@Repository
public interface UserDao {

    @Select("select * from user")
    public List<User> selectAllUser();

    @Insert("insert into user(id,name) values(#{user.id},#{user.name})")
    public int  insertUser(@Param("user") User user);
}
