package com.neuedu.test;

import com.neuedu.pojo.Shipping;
import com.neuedu.pojo.UserInfo;
import com.neuedu.utils.BigDecimalUtils;

import java.math.BigDecimal;

public class Test {

    public static void main(String[] args) {
//        BigDecimal bigDecimal = new BigDecimal("0.05");
//        BigDecimal bigDecimal1=new BigDecimal("0.1");
//
//        System.out.println(bigDecimal.add(bigDecimal1));
//
//        System.out.println(BigDecimalUtils.add(0.3,0.5));
//        System.out.println(BigDecimalUtils.sub(0.3,0.5));
//        System.out.println(BigDecimalUtils.mul(0.3,0.5));
//        System.out.println(BigDecimalUtils.div(0.13,100));

        String s=new String();
        if(s==null){
            System.out.println("null");
        }else{
            System.out.println("notnull");
        }
        if(s.equals("")){
            System.out.println("空");
        }else{
            System.out.println("非空");
        }

//        UserInfo userInfo = new UserInfo();
//        if(userInfo==null){
//            System.out.println("null");
//        }else{
//            System.out.println("notnull");
//        }
//        if(userInfo.equals("")){
//            System.out.println("空");
//        }else{
//            System.out.println("非空");
//        }

    }

}
