package com.example.validation.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @program: learn
 * @description:
 * @author: reshui
 * @create: 2020-11-19 00:59
 **/
@Data
public class UserForNormal {
    String id;

    @NotNull
    @Length(min=3,max=8)
    String name;
    @NotNull
    @Email
    String email;

    @Min(value = 18,message = "未满18哟")
    @Max(value = 24,message = "超过24咯")
    Integer age;

}
