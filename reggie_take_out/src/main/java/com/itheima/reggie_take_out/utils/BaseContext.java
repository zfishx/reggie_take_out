package com.itheima.reggie_take_out.utils;
//基于ThreadLocal封装数据类，用户保存和获取当前登录用户id

public class BaseContext {
    private static ThreadLocal<Long> tl=new ThreadLocal<>();
    public static void setid(Long id){
        tl.set(id);
    }
    public static Long getid(){
        return tl.get();
    }

}
