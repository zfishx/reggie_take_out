package com.itheima.reggie_take_out.utils;
//自定义业务异常类
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
