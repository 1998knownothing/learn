package com.example.apilimit.controller;

import com.example.apilimit.annotation.AccessLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-15 18:08
 **/
@RestController
public class TestController {

    @AccessLimit
    @GetMapping("/get1")
    public Object get(){
        System.out.println("get1");
        Map<String,Object> map=new HashMap<>();
        map.put("code",2021);
        map.put("msg","success get1");
        return map;
    }

    @GetMapping("get2")
    public Object get2(){
        System.out.println("get2");
        Map<String,Object> map=new HashMap<>();
        map.put("code",2022);
        map.put("msg","success get2");
        return map;
    }
}
