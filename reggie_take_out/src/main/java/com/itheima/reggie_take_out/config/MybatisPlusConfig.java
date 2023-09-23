package com.itheima.reggie_take_out.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor i = new MybatisPlusInterceptor();
        i.addInnerInterceptor(new PaginationInnerInterceptor());
        return i;
    }
}
