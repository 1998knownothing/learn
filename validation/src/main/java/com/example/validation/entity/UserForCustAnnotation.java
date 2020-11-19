package com.example.validation.entity;

import com.example.validation.annotation.IsUniqueId;
import lombok.Data;

/**
 * @program: learn
 * @description:
 * @author: reshui
 * @create: 2020-11-19 23:28
 **/
@Data
public class UserForCustAnnotation {

    @IsUniqueId
    String id;
}
