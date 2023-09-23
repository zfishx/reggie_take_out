package com.itheima.reggie_take_out.config;

import com.itheima.reggie_take_out.utils.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    //配置静态资源的路径
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置静态资源的路径
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/static/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/static/front/");
    }
    //扩展mvc框架的消息转换器
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建一个消息转换器，将controller返回的对象转换为json字符串
        MappingJackson2HttpMessageConverter mc = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用的是jackson将java对象转换为json字符串
        mc.setObjectMapper(new JacksonObjectMapper());
        //将消息转换器添加到mvc框架中
        converters.add(0,mc);//把自己的转换器放到最前面

    }
}
