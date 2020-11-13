package com.example.operlog.util;


import java.io.PrintWriter;
import java.io.StringWriter;
/**
 * @program: learn
 * @description: 异常工具
 * @author: Mr.liu
 * @create: 2020-11-13 17:08
 **/

public class ThrowableUtil {

    /**log.setExceptionDetail(ThrowableUtil.getStackTrace(e).getBytes());
     * 获取堆栈信息
     */
    public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
