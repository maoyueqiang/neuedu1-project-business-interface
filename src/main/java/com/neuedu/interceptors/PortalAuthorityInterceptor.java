package com.neuedu.interceptors;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 拦截后台请求，验证用户是否登录
 */
@Component
public class PortalAuthorityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        System.out.println("====preHandle===");

        HttpSession session = request.getSession();
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            response.reset();
            try {
                //System.out.println(response.getHeader("Content-Type"));

                //解决返回值乱码问题
                response.setHeader("Content-Type","text/html;charset=UTF-8");

                PrintWriter writer = response.getWriter();
                ServerResponse serverResponse = ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
                String json = serverResponse.toString();
                writer.write(json);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  false;
        }

        //不拦截请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        System.out.println("====postHandle===");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("====afterHandle===");
    }
}
