package com.neuedu.utils;

import java.math.BigDecimal;

/**
 * 价格工具类
 */

public class BigDecimalUtils {
    /**
     * 加法运算
     */
    public static BigDecimal add(double d1,double d2){
        BigDecimal bigDecima=new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal=new BigDecimal(String.valueOf(d2));
        return bigDecima.add(bigDecimal);
    }

    /**
     * 减法运算
     */
    public static BigDecimal sub(double d1,double d2){
        BigDecimal bigDecima=new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal=new BigDecimal(String.valueOf(d2));
        return bigDecima.subtract(bigDecimal);
    }

    /**
     * 乘法运算
     */
    public static BigDecimal mul(double d1,double d2){
        BigDecimal bigDecima=new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal=new BigDecimal(String.valueOf(d2));
        return bigDecima.multiply(bigDecimal);
    }

    /**
     * 除法运算
     * 保留两位小数
     * 四舍五入
     */
    public static BigDecimal div(double d1,double d2){
        BigDecimal bigDecima=new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal=new BigDecimal(String.valueOf(d2));
        return bigDecima.divide(bigDecimal,2,BigDecimal.ROUND_HALF_UP);
    }

}
