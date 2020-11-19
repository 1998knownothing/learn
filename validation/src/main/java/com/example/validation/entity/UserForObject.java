package com.example.validation.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @program: learn
 * @description:
 * @author: reshui
 * @create: 2020-11-19 02:22
 **/
//实际场景中，有可能某个字段也是一个对象，这种情况先，可以使用嵌套校验。
    //必须标记 @Valid 注解
    @Data
public class UserForObject {

    @Length(min = 2, max = 10)
    private String userName;

    @NotNull
    @Valid
    private Job job;

    @Data
    public static class Job {

        @Length(min = 2, max = 10)
        private String jobName;

    }
}
