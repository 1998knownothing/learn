package com.example.operlog.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-13 15:03
 **/
@Data
public class OperationLog {
    private String operId;//主键
    private String module;//功能模块
    private String type;//操作类型
    private String requestMethod;//请求方式
    private String operDesc;//操作描述
    private String operUserAgent;//操作描述
    private String operRequstParam;//请求参数
    private String operResponseParam;//返回参数
    private String operUserId;//操作员id
    private String operUsername;//操作员名称
    private String operLocation;//操作地点
    private Integer status;//操作状态
    private String operMethod;//操作方法
    private String operURL;//请求URL
    private String operIp;//请求ID
    private String excName;//异常名称
    private String errorMsg;//错误信息
    private byte[] msg;
    private Date createTime;//操作时间
    private Long costTime;//耗时


}
