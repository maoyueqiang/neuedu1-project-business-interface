package com.neuedu;

import com.google.common.collect.Lists;
import com.neuedu.interceptors.AdminAuthorityInterceptor;
import com.neuedu.interceptors.PortalAuthorityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootConfiguration
public class MySpringBootConfig implements WebMvcConfigurer{

    //注入拦截器
    @Autowired
    AdminAuthorityInterceptor adminAuthorityInterceptor;
    @Autowired
    PortalAuthorityInterceptor portalAuthorityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        //添加前台拦截器
            //第一种写法
//        registry.addInterceptor(portalAuthorityInterceptor)
//                .addPathPatterns("/shipping/**")
//                .addPathPatterns("/cart/**")
//                .addPathPatterns("/order/**")
//                .excludePathPatterns("/order/callback.do")
//                .addPathPatterns("/user/**")
//                //user模块有很多需要排除
//                .excludePathPatterns("/user/login.do","/user/register.do",
//                        "/user/forget_getQuestion.do","/user/forget_checkAnswer.do",
//                        "/user/forget_resetPassword.do","/user/check_valid.do");
            //第二种写法
        //定义要拦截的路径
        List<String> addPatterns = Lists.newArrayList();
        addPatterns.add("/shipping/**");
        addPatterns.add("/cart/**");
        addPatterns.add("/order/**");
        addPatterns.add("/user/**");
        //定义要排除的路径
        List<String> excludePatterns = Lists.newArrayList();
        excludePatterns.add("/user/login.do");
        excludePatterns.add("/user/register.do");
        excludePatterns.add("/user/forget_getQuestion.do");
        excludePatterns.add("/user/forget_checkAnswer.do");
        excludePatterns.add("/user/forget_resetPassword.do");
        excludePatterns.add("/user/check_valid.do");
        excludePatterns.add("/order/callback.do");
        registry.addInterceptor(portalAuthorityInterceptor)
                .addPathPatterns(addPatterns)
                .excludePathPatterns(excludePatterns);


        //添加后台拦截器
        registry.addInterceptor(adminAuthorityInterceptor)
                .addPathPatterns("/manage/**") //要拦截的路径
                .excludePathPatterns("/manage/user/login.do"); //排除掉某些路径


    }
}
