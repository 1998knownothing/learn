package com.example.operlog.common;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-12 23:59
 **/
public enum OperType {

    //操作类型
    TEST("测试"),
    ADD("新增"),DELETE("删除"),QUERY("查询")
    ;

    private String type;

    OperType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }}
