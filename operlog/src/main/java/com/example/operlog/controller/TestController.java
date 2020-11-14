package com.example.operlog.controller;

import com.example.operlog.annotation.Log;
import com.example.operlog.common.OperType;
import com.example.operlog.entity.User;
import com.example.operlog.util.CommonUtil;
import com.example.operlog.util.IpUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-12 23:58
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    //在带注解，抛出异常
    @GetMapping("/1")
    @Log(operModule = "查询",operDesc = "就查查而已",operType = OperType.QUERY)
    public void get(){
        throw new RuntimeException("test");

    }
    //注解，正常执行
    @GetMapping("/2")
    @Log(operModule = "查询",operDesc = "就查查而已222222",operType = OperType.QUERY)
    public void get2(){
        System.out.println("=======get2");

    }
    //不带注解，抛出异常
    @GetMapping("/3")
    public void get3(){
        throw new RuntimeException("get3");

    }
    //不带注解，正常执行与返回数据
    @GetMapping("/4")
    public Object get4(){
        System.out.println("=======get4");
        return new User("123","aaaaaa");
    }
    //带文件参数测试
    @PostMapping("/file")
    @Log
    public void uploadFile(String param,MultipartFile[] file){
        System.out.println("=======upload file");
    }
    @PostMapping("/file1")
    @Log
    public void uploadFile1(@RequestBody User param){
        System.out.println("=======upload file1");
    }
    @GetMapping("/5")
    @Log
    public Object uploadFile5(){

        String address = IpUtils.getAddress("223.73.198.16");
        return address;
    }
}
