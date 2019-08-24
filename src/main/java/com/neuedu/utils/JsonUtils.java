package com.neuedu.utils;



import com.google.common.collect.Lists;
import com.neuedu.pojo.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;


public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static{
        //对象中所有字段序列化
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);

        //取消默认的timestamp格式（原本默认时间会为时间戳格式）
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);

        //原本空字符串转json会出错，现在忽略空bean转json错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);

        //设置时间格式  使用的是DateUtils中的格式
        objectMapper.setDateFormat(new SimpleDateFormat(DateUtils.STANDARD_FORMATE));

        //json中存在的属性Java中可能不存在，忽略这些属性，防止出错
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /**
     * 将对象转成字符串
     * 个人感觉和toString方法一样
     */
    public static <T>String obj2String(T obj){
        //非空校验
        if(obj==null){
            return null;
        }

        //判断obj为字符串类型？返回obj：转换后返回
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将字符串转对象
     * 格式化，一个属性一行，好看，舒畅
     */
    public static <T>String obj2StringPretty(T obj){
        //非空校验
        if(obj==null){
            return null;
        }

        //判断obj为字符串类型？返回obj：转换后返回
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 字符串转对象
     */
    public static <T> T string2obj(String str,Class<T> clazz){
        //非空校验
        if(StringUtils.isEmpty(str)||clazz==null){
            return null;
        }

        //先判断，根据结果返回
        try {
            return clazz.equals(String.class)? (T) str : objectMapper.readValue(str,clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 字符串(json数组)转集合
     */
    public static <T> T string2obj(String str, TypeReference <T>typeReference){
        //非空校验
        if(StringUtils.isEmpty(str)||typeReference==null){
            return null;
        }

        //先判断，根据结果返回
        try {
            return typeReference.getType().equals(String.class)? (T) str : objectMapper.readValue(str,typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 字符串(json数组)转集合(集合中类型为map等)
     */
    public static <T> T string2obj(String str,Class<?> collectionClass,Class<?>... elements){

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elements);

        try {
            return objectMapper.readValue(str,javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("zhangsan");
        userInfo.setPassword("123");

        UserInfo userInfo1 = new UserInfo();
        userInfo1.setUsername("lisi");
        userInfo1.setPassword("1234");

        List<UserInfo> userInfoList = Lists.newArrayList();
        userInfoList.add(userInfo);
        userInfoList.add(userInfo1);

        String s = obj2StringPretty(userInfoList);
        System.out.println(s);

        List<UserInfo> userInfoList1 = string2obj(s, new TypeReference<List<UserInfo>>() {});
        System.out.println(userInfoList1);

        List<UserInfo> userInfoList2 = string2obj(s,List.class,UserInfo.class);
        System.out.println(userInfoList2);
    }


}
