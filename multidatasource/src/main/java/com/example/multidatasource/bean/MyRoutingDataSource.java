package com.example.multidatasource.bean;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * @program: learn
 * @description:获取路由key
 **/
public class MyRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    @Nullable
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.get();
    }
}
