package com.example.wxlogin.entity;

import lombok.Data;

/**
 * @program: learn
 * @description:
 * @author: reshui
 * @create: 2020-11-21 14:12
 **/
@Data
public class User {
    private String nickName;
    private int gender;
    private String avatarUrl;
    private String session_key;
    private String openid;
}
