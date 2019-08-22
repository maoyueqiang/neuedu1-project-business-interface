package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.config.AppConfig;
import com.neuedu.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController //controller下所有方法返回值都为字符串
public class HelloController {

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    //@ResponseBody   //返回字符串需要添加的注解
    public UserInfo hello(UserInfo userInfo){
        return userInfo;
    }

    @RequestMapping(value = "/res",method = RequestMethod.GET)
    //@ResponseBody   //返回字符串需要添加的注解
    public ServerResponse res(){
        return ServerResponse.createServerResponseBySuccess(null,"hello");
    }

//    @Value("${jdbc.driver}")
//    private String driver;
//    @Value("${jdbc.username}")
//    private String username;
//    @Value("${jdbc.password}")
//    private String password;
//
//    @RequestMapping(value = "/driver",method = RequestMethod.GET)
//    public String getDriver(){
//        return driver+" "+username+" "+password;
//    }

    //使用注解来获取配置文件中属性值
    @Autowired
    AppConfig appConfig;

    @RequestMapping(value = "/driver",method = RequestMethod.GET)
    public String getDriver(){
        return appConfig.getDriver()+" "+appConfig.getUsername()+" "+appConfig.getPassword();
    }
}
