package com.example.multidatasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.example.multidatasource.bean.MyRoutingDataSource;
import com.example.multidatasource.type.DBTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.rmi.runtime.Log;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-16 00:28
 **/
@Configuration
@Slf4j
public class DataSourceConfig {

    /*
    * HikariDataSource改为druid
    * */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource(){
        return DruidDataSourceBuilder.create().build();
    }
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave1")
    public DataSource slave1DataSource(){
        return DruidDataSourceBuilder.create().build();
    }
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave2")
    public DataSource slave2DataSource(){
        return DruidDataSourceBuilder.create().build();
    }
    @Bean
    public DataSource myRoutingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
    @Qualifier("slave1DataSource") DataSource slave1DataSource,
    @Qualifier("slave2DataSource") DataSource slave2DataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.MASTER, masterDataSource);
        targetDataSources.put(DBTypeEnum.SLAVE1, slave1DataSource);
        targetDataSources.put(DBTypeEnum.SLAVE2, slave2DataSource);
        log.info(targetDataSources.toString());
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        return myRoutingDataSource;
    }

/*
    SpringBoot 默认使用 HikariDataSource
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource(){
        log.info(DataSourceBuilder.create().build().toString());
        return DataSourceBuilder.create().build();
    }
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave1")
    public DataSource slave1DataSource(){
        return DataSourceBuilder.create().build();
    }
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave2")
    public DataSource slave2DataSource(){
        return DataSourceBuilder.create().build();
    }*/

/*    spring.datasource.master.jdbc-url= jdbc:mysql://localhost:3306/db01?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
    spring.datasource.master.username= root
    spring.datasource.master.password= 1158982120fire
    spring.datasource.master.driver-class-name= com.mysql.jdbc.Driver

    spring.datasource.slave1.jdbc-url= jdbc:mysql://localhost:3306/db02?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
    spring.datasource.slave1.username= root
    spring.datasource.slave1.password= 1158982120fire
    spring.datasource.slave1.driver-class-name= com.mysql.jdbc.Driver

    spring.datasource.slave2.jdbc-url= jdbc:mysql://localhost:3306/db03?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
    spring.datasource.slave2.username= root
    spring.datasource.slave2.password= 1158982120fire
    spring.datasource.slave2.driver-class-name= com.mysql.jdbc.Driver*/

}
