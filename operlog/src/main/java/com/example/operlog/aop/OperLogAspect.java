package com.example.operlog.aop;

import com.alibaba.fastjson.JSON;
import com.example.operlog.annotation.Log;
import com.example.operlog.entity.OperationLog;
import com.example.operlog.entity.User;
import com.example.operlog.util.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @program: learn
 * @description: aop切入类
 * @author: Mr.liu
 * @create: 2020-11-12 23:56
 **/
@Aspect
@Component
@Slf4j
public class OperLogAspect {

    ThreadLocal<Long> currentTime = new ThreadLocal<>();
    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.example.operlog.annotation.Log)")
    public void operLogPointCut() {
    }

    /**
     * 设置操作异常切入点记录异常日志 扫描所有controller包下操作
     */
    @Pointcut("execution(* com.example.operlog.controller..*.*(..))")
    public void operExceptionLogPointCut() {
    }

    @Around("operLogPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        //正常处理开始
        result = joinPoint.proceed();
        //正常处理结束
        Long time=System.currentTimeMillis() - currentTime.get();
        currentTime.remove();

        handleLog(joinPoint,null,result,time);

        return result;
    }


    @AfterThrowing(pointcut = "operExceptionLogPointCut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Exception e) {
       handleLog(joinPoint,e,null,null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult,Long time){
        try
        {

            // 获取RequestAttributes
   /*         RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            // 从获取RequestAttributes中获取HttpServletRequest的信息
            HttpServletRequest request = (HttpServletRequest) requestAttributes
                    .resolveReference(RequestAttributes.REFERENCE_REQUEST);*/
            HttpServletRequest request = RequestHolder.getHttpServletRequest();

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            //获取注解
            Log  aopLog = method.getAnnotation(Log.class);


            // 获取当前的用户
            User currentUser = GetUserUtils.getCurrentUser();

            OperationLog operLog = new OperationLog();
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            operLog.setOperId(id);
            operLog.setOperURL(request.getRequestURI());
            operLog.setStatus(1);//成功
            operLog.setCreateTime(new Date());
            operLog.setCostTime(time);
            operLog.setOperUserAgent(CommonUtil.getUserAgent(request));
            // 请求的地址
            operLog.setOperIp(CommonUtil.getIp(request));

            // 返回参数
            operLog.setOperResponseParam(JSON.toJSONString(jsonResult));

            if (currentUser != null)
            {
                operLog.setOperUserId(currentUser.getUserId());
                operLog.setOperUsername(currentUser.getUsername());
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            //设置请求方法名
            operLog.setOperMethod(className + "." + methodName + "()");
            // 设置请求方式GET..
            operLog.setRequestMethod(request.getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, aopLog, operLog);

            operLog.setOperLocation(IpUtils.getAddress(operLog.getOperIp()));
            //利用是否有异常定性记录失败信息
            if (e != null)
            {
                operLog.setStatus(0);//失败
                operLog.setExcName(e.getClass().getName());
                operLog.setErrorMsg(ThrowableUtil.getStackTrace(e));

                log.error("耗时:{}  用户id:{}   用户名username: {}  请求ip:{}  User-Agent:{} 方法路径:{} 方法参数:{}",
                        operLog.getCostTime(),
                        operLog.getOperUserId(),
                        operLog.getOperUsername(),
                        operLog.getOperId(),
                        operLog.getOperUserAgent(),
                        methodName,
                        operLog.getOperRequstParam());
                log.error("==控制层方法通知异常==");
                log.error("异常信息:{}", e.getMessage());
                //e.printStackTrace();
                return;
            }
            log.info(JSON.toJSONString(operLog));
            // 保存数据库
            //可以借助异步持久化AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
            log.info("耗时:{}  用户id:{}   用户名username: {}  请求ip:{}  User-Agent:{} 方法路径:{} 方法参数:{}",
                    operLog.getCostTime(),
                    operLog.getOperUserId(),
                    operLog.getOperUsername(),
                    operLog.getOperId(),
                    operLog.getOperUserAgent(),
                    methodName,
                    operLog.getOperRequstParam());
        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }

    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log 日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, OperationLog operLog) throws Exception
    {
        if(log!=null){
            operLog.setModule(log.operModule());
            operLog.setType(log.operType().getType());
            operLog.setOperDesc(log.operDesc());
        }else {
            //用于无注解的控制层方法 异常抛出 记录
            operLog.setModule("无默认模块");
            operLog.setType("无默认类别");
            operLog.setOperDesc("无默认描述");
        }

        // 获取参数的信息
        setRequestValue(joinPoint, operLog);
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, OperationLog operLog) throws Exception
    {
        String params = argsArrayToString(joinPoint.getArgs());
        //避免过长的无用信息
        operLog.setOperRequstParam(StringUtil.substring(params, 0, 2000));
    }
    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray)
    {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0)
        {
            for (int i = 0; i < paramsArray.length; i++)
            {

                //排除不需要记录的参数
                if (!isFilterObject(paramsArray[i])){

                    Object value=paramsArray[i];
                    if (value instanceof MultipartFile) {
                        MultipartFile file = (MultipartFile) value;
                        Object jsonObj = JSON.toJSON(file.getOriginalFilename());
                        params.append(jsonObj.toString()).append(" ");
                    }
                    else if (value instanceof MultipartFile[]) {
                        MultipartFile[] files = (MultipartFile[]) value;
                        for(MultipartFile file:files){
                            Object jsonObj = JSON.toJSON(file.getOriginalFilename());
                            params.append(jsonObj.toString()).append(" ");
                        }
                    }else{
                        Object jsonObj = JSON.toJSON(value);
                        params.append(jsonObj.toString()).append(" ");
                    }
                }
            }
        }
        return params.toString();
    }


    /**
     * 判断是否需要过滤
     * @param o 对象信息。
     * @return 是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o)
    {
        return o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }



    /**
     *
     * 缺点无法 获取文件信息  只有更适合
     *             // 请求的参数
     *             Map<String, String> rtnMap = converMap(request.getParameterMap());
     *             // 将参数所在的数组转换成json
     *             String params = JSON.toJSONString(rtnMap);
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

/*
    //用于未定义log注解，抛出异常也需要记录耗时情况
    @Before("operExceptionLogPoinCut()")
    public void beforeMethod(JoinPoint joinPoint){
        currentTime.set(System.currentTimeMillis());
    }
    @After("operExceptionLogPoinCut()")
    public void afterMethod(JoinPoint joinPoint){
        currentTime.remove();
    }*/

/*    StringBuilder params = new StringBuilder("{");
    //参数值
    Object[] argValues = joinPoint.getArgs();
    //参数名称
    String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        if(argValues != null){
        for (int i = 0; i < argValues.length; i++) {
            params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
        }
            *//*
            *             //如果是文件对象
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                value = file.getOriginalFilename();  //获取文件名
            }
            * *//*

        params.append("}");
    }
    StringBuffer loginfo=new StringBuffer();*/

}
