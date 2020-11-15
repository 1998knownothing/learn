package com.example.apilimit.interceptor;

import com.example.apilimit.annotation.AccessLimit;
import com.example.apilimit.util.IpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-15 18:05
 **/
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {
    @Resource
    private RedisTemplate<String, Integer> redisTemplate;  //使用RedisTemplate操作redis
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (!method.isAnnotationPresent(AccessLimit.class)) {
                return true;
            }
            AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int limit = accessLimit.limit();
            int sec = accessLimit.sec();
            String key = IpUtils.getIpAddr(request) + request.getRequestURI();
            Integer maxLimit = redisTemplate.opsForValue().get(key);
            if (maxLimit == null) {
                redisTemplate.opsForValue().set(key, 1, sec, TimeUnit.SECONDS);  //set时一定要加过期时间
            } else if (maxLimit < limit) {
                redisTemplate.opsForValue().set(key, maxLimit + 1, sec, TimeUnit.SECONDS);
            } else {
                output(response, "请求太频繁，请稍等再试!");
                return false;
            }
        }
        return true;
    }
    public void output(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        Map<String,Object> map=new HashMap<>();
        map.put("code",2020);
        map.put("msg",msg);
        ObjectMapper om = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.write(om.writeValueAsString(map));
        out.flush();
        out.close();
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
