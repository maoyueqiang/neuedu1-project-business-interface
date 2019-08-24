package com.neuedu.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    private static final String COOKIE_DOMAIN=".zykzxp.ltd";

    /**
     * 添加cookie
     */
    public static void writeCookie(HttpServletResponse response,
                                   String cookieName,
                                   String cookieValue){
        Cookie cookie = new Cookie(cookieName,cookieValue);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");//根目录
        cookie.setHttpOnly(true);//通过脚本无法获取cookie
        cookie.setMaxAge(60*60*24*7);
        response.addCookie(cookie);
    }

    /**
     * 读取cookie
     */
    public String readCookie(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>0){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals(cookieName)){
                    return cookie.getValue();
                }
            }
        }

        return null;
    }


    /**
     * 删除cookie
     */
    public String deleteCookie(HttpServletRequest request,HttpServletResponse response,String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>0){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals(cookieName)){
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);

                    response.addCookie(cookie);
                }
            }
        }
    }

}
