package com.example.validation.conifg;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


/**
 * @program: learn
 * @description:
 * @author: reshui
 * @create: 2020-11-19 23:36
 **/
@Configuration
public class FailFastConfig {

/*    快速失败 (Fail Fast)
    Spring Validation 默认会校验完所有字段，然后才抛出异常。
    可以通过一些简单的配置，开启 Fali Fast 模式，一旦校验失败就立即返回。*/
    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory= Validation.byProvider(HibernateValidator.class)
                .configure()
                //快速失败模式
        .failFast(true)
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}
