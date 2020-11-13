package com.example.operlog.annotation;

import com.example.operlog.common.OperType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-12 23:55
 **/
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
public @interface Log {
    String operModule() default ""; // 操作模块
    OperType operType() default OperType.ADD;  // 操作类型
    String operDesc() default "";  // 操作说明
    /**
     * 是否保存返回的参数
     */
    boolean isSaveResponseData() default true;
}
