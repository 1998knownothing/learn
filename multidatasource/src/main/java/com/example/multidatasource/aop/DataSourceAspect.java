package com.example.multidatasource.aop;

import com.example.multidatasource.bean.DBContextHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-16 01:18
 **/
@Aspect
@Component
public class DataSourceAspect {

    @Pointcut("!@annotation(com.example.multidatasource.annotation.Master) " +
            "&& (execution(* com.example.multidatasource.service..*.select*(..)) " +
            "|| execution(* com.example.multidatasource.service..*.get*(..)))")
    public void readPointcut(){

    }
    @Pointcut("@annotation(com.example.multidatasource.annotation.Master) " +
            "|| execution(* com.example.multidatasource.service..*.insert*(..)) " +
            "|| execution(* com.example.multidatasource.service..*.add*(..)) " +
            "|| execution(* com.example.multidatasource.service..*.update*(..)) " +
            "|| execution(* com.example.multidatasource.service..*.edit*(..)) " +
            "|| execution(* com.example.multidatasource.service..*.delete*(..)) " +
            "|| execution(* com.example.multidatasource.service..*.remove*(..))")
    public void writePointcut(){

    }
    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }


}
