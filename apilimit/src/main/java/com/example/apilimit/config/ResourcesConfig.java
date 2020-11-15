package com.example.apilimit.config;

import com.example.apilimit.interceptor.AccessLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-15 18:05
 **/
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {
    @Autowired
    AccessLimitInterceptor accessLimitInterceptor;
    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(accessLimitInterceptor).addPathPatterns("/**");
    }
}
