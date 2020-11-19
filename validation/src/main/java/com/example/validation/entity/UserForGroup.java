package com.example.validation.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @program: learn
 * @description:
 * @author: reshui
 * @create: 2020-11-19 02:18
 **/
@Data
public class UserForGroup {

    @Min(value = 10, groups = Update.class)
    String id;

    @NotNull(groups = {Save.class, Update.class})
    @Length(min = 2, max = 10, groups = {Save.class, Update.class})
    String name;

    @NotNull(groups = {Save.class})
    String email;


    public interface Save {
    }
    public interface Update {
    }
}
