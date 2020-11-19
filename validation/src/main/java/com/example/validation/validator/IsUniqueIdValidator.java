package com.example.validation.validator;

import com.example.validation.annotation.IsUniqueId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @program: learn
 * @description:
 * @author: reshui
 * @create: 2020-11-19 22:55
 **/
public class IsUniqueIdValidator implements ConstraintValidator<IsUniqueId,String>
{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        if(value!=null){

            return true;
        }
        return false;
    }
}
