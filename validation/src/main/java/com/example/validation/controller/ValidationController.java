package com.example.validation.controller;

import com.example.validation.entity.UserForCustAnnotation;
import com.example.validation.entity.UserForGroup;
import com.example.validation.entity.UserForNormal;
import com.example.validation.entity.UserForObject;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @program: learn
 * @description:
 * @author: reshui
 * @create: 2020-11-19 01:01
 **/
@Validated
@RestController
@RequestMapping("/v")
public class ValidationController {
//推荐将一个个参数平铺到方法入参中。
// 在这种情况下，必须在 Controller 类上标注 @Validated 注解，
// 并在入参上声明约束注解 (如 @Min 等)。
// 如果校验失败，会抛出 ConstraintViolationException 异常。
    @GetMapping("/1")
    public Object get1(@Length(min=6,max = 10)@NotNull String str,
                       @NotNull String a){
        return str;
    }

    @PostMapping("/2")
    public Object save(@Validated @RequestBody UserForNormal user){

        return "2";
    }


    /*
    @Validated 注解上指定校验分组
    * */

    @PostMapping("/save")
    public Object saveUser(@RequestBody @Validated(UserForGroup.Save.class) UserForGroup user) {
        // 校验通过，才会执行业务逻辑处理
        return "save";
    }

    @PostMapping("/update")
    public Object updateUser(@RequestBody @Validated(UserForGroup.Update.class) UserForGroup user) {
        // 校验通过，才会执行业务逻辑处理
        return "update";
    }

    @PostMapping("/custom/val")
    public Object customValidator(@RequestBody @Validated UserForCustAnnotation user) {
        // 校验通过，才会执行业务逻辑处理
        return "custom/annotation";
    }
/*    嵌套校验
    前面的示例中，DTO 类里面的字段都是基本数据类型和 String 类型。
    但是实际场景中，有可能某个字段也是一个对象，这种情况先，可以使用嵌套校验。*/
    @PostMapping("/qiantao")
    public Object customValidator(@RequestBody @Validated UserForObject user) {
        // 校验通过，才会执行业务逻辑处理
/*        {
            "userName":"22",
                "job":
            {
                "jobName":"3"
            }
        }*/
        return "qiantao";
    }

    /*
    * 编程式校验
        上面的示例都是基于注解来实现自动校验的，
        在某些情况下，我们可能希望以编程方式调用验证。
        这个时候可以注入 javax.validation.Validator 对象，
        然后再调用其 api。
    *
    * */

    @Resource
    private javax.validation.Validator globalValidator;

    // 编程式校验
    @PostMapping("/saveWithCodingValidate")
    public Object saveWithCodingValidate(@RequestBody UserForGroup userDTO) {
        Set<ConstraintViolation<UserForGroup>> validate = globalValidator.validate(userDTO, UserForGroup.Save.class);
        // 如果校验通过，validate为空；否则，validate包含未校验通过项
        if (validate.isEmpty()) {
            // 校验通过，才会执行业务逻辑处理

        } else {
            for (ConstraintViolation<UserForGroup> userDTOConstraintViolation : validate) {
                // 校验失败，做其它逻辑
                System.out.println(userDTOConstraintViolation);
            }
        }
        return "编程方式调用验证";
    }

}
