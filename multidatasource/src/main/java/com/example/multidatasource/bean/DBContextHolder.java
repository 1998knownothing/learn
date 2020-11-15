package com.example.multidatasource.bean;

import com.example.multidatasource.type.DBTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-16 01:00
 **/
@Slf4j
@Component
public class DBContextHolder {

    /*
    * contextHolder 是线程变量，因为每个请求是一个线程，所以通过这样来区分使用哪个库
      determineCurrentLookupKey是重写的AbstractRoutingDataSource的方法，
      主要是确定当前应该使用哪个数据源的key，因为AbstractRoutingDataSource 中保存的多个数据源是通过Map的方式保存的
    * */
    private static final ThreadLocal<DBTypeEnum> contextHolder=new ThreadLocal<>();

    private static final AtomicInteger counter =new AtomicInteger(-1);

    //设置当前线程所用数据库类型
    public static void set(DBTypeEnum dbType){
        contextHolder.set(dbType);
    }

    //获取当前线程所用数据类型
    public static DBTypeEnum get() {
        return contextHolder.get();
    }
    public static void master() {
        set(DBTypeEnum.MASTER);
        log.info("切换到master");
    }

    public static void slave() {
        //  轮询
        int index = counter.getAndIncrement() % 2;
        if (counter.get() > 9999) {
            counter.set(-1);
        }
        if (index == 0) {
            set(DBTypeEnum.SLAVE1);
            log.info("切换到slave1");
        }else {
            set(DBTypeEnum.SLAVE2);
            log.info("切换到slave2");
        }
    }

}
