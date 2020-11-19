package com.example.validation.annotation;

import com.example.validation.validator.IsUniqueIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @program: learn
 * @description:
 * @author: reshui
 * @create: 2020-11-19 22:52
 **/
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IsUniqueIdValidator.class})
public @interface IsUniqueId {
    //默认错误信息提示
    String message() default "非独特id";
    //分组
    Class<?>[] groups() default {};

    //负载
    Class<? extends Payload>[] payload() default {};
}
