package com.example.wxlogin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wxlogin.entity.User;
import com.example.wxlogin.utils.HttpClientUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: learn
 * @description:
 * @author: reshui
 * @create: 2020-11-21 14:12
 **/
@RestController
public class WxLoginController {

    @PostMapping("/wxlogin")
    public Object wxlogin(String code,String userJsonStr){
        //https://api.weixin.qq.com/sns/jscode2session
        // ?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code
        String url="https://api.weixin.qq.com/sns/jscode2session";
        Map<String,String> param=new HashMap<>();
        Map<String,String> returnMap=new HashMap<>();
        param.put("appid","wxa76d3fbc98b60a36");
        param.put("secret","a90891e08895023634311107c8298a4a");
        param.put("js_code",code);
        param.put("grant_type","authorization_code");
        System.out.println(code);
        System.out.println(userJsonStr);
        String httpResult = HttpClientUtil.doGet(url, param);
        JSONObject object = JSON.parseObject(httpResult);
        String openid=(String)object.get("openid");
        String session_key=(String)object.get("session_key");
        if("".equals(object.get("openid"))||null==object.get("openid")){
            returnMap.put("msg","服务器远程获取用户凭证失败");
            return returnMap;
        }

        User user = JSONObject.parseObject(userJsonStr, User.class);
        user.setOpenid(openid);
        user.setSession_key(session_key);
        System.out.println(user.toString());
        returnMap.put("msg","成功登录");
        return returnMap;
    }

}
