package com.example.validation.handler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @program: learn
 * @description:
 * @author: reshui
 * @create: 2020-11-19 01:38
 **/
@RestControllerAdvice
public class CommonExceptionHandler {


    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        Map map=new HashMap<String,Object>();
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();

        map.put("code",5001);
        map.put("msg",msg);
        return map;
    }
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> handleConstraintViolationException(ConstraintViolationException ex) {
        Map map=new HashMap<String,Object>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = violations.iterator();
        StringBuilder sb = new StringBuilder("校验失败:");
        while (iterator.hasNext()){
            ConstraintViolation<?> next = iterator.next();
            sb.append(next.getPropertyPath().toString().split("\\.")[1]).append("：").append(next.getMessage()).append(", ");
        }


        map.put("code",5001);
        map.put("msg",sb.toString());
        return map;
    }

}
