package com.neuedu.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 服务端响应对象
 */
//注解：对象中不是空值的属性才会保留
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {

    private  int status;//状态 0:成功   !0:各种错误
    private String msg;//接口的返回信息
    private T data;//接口返回给前端的数据

    private ServerResponse(){

    }

    private ServerResponse(int status){
        this.status=status;
    }
    //status不为0，返回错误信息
    private ServerResponse(int status,String msg){
        this.status=status;
        this.msg=msg;
    }
    //status为0，返回数据
    private ServerResponse(int status,T data){
        this.status=status;
        this.data=data;
    }
    //status为0，返回数据和信息
    private ServerResponse(int status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }

    /**
     * 判断接口是否成功调用
     */
    @JsonIgnore
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS;
    }

    //接口调用成功
    public static ServerResponse  createServerResponseBySuccess(){
        return new ServerResponse<>(ResponseCode.SUCCESS);
    }
    public static<T> ServerResponse  createServerResponseBySuccess(T data){
        return new ServerResponse<>(ResponseCode.SUCCESS,data);
    }
    public static ServerResponse  createServerResponseBySuccess(String msg){
        return new ServerResponse<>(ResponseCode.SUCCESS,msg);
    }
    public static<T> ServerResponse  createServerResponseBySuccess(String msg,T data){
        return new ServerResponse<>(ResponseCode.SUCCESS,msg,data);
    }

    //调用失败
    public static ServerResponse createServerResponseByFail(){
        return new ServerResponse<>(ResponseCode.ERROR);
    }
    public static ServerResponse createServerResponseByFail(String msg){
        return new ServerResponse<>(ResponseCode.ERROR,msg);
    }
    public static ServerResponse createServerResponseByFail(int status){
        return new ServerResponse<>(status);
    }
    public static ServerResponse createServerResponseByFail(int status,String msg){
        return new ServerResponse<>(status,msg);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
