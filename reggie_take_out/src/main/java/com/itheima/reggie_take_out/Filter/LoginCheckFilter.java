package com.itheima.reggie_take_out.Filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNullFormatVisitor;
import com.itheima.reggie_take_out.utils.BaseContext;
import com.itheima.reggie_take_out.utils.R;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest)servletRequest;
        HttpServletResponse resp=(HttpServletResponse)servletResponse;
        String uri = req.getRequestURI().toString();
        System.out.println("请求uri:"+uri);
        if(uri.contains("/employee/login")||uri.contains("/employee/logout") || uri.matches("/backend/.*") || uri.matches("/front/.*") || uri.contains("/user/sendMsg") ||uri.contains("/user/login")){
            System.out.println("放行"+uri);
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        //判断是否员工登录
        if(req.getSession().getAttribute("employee")!=null){
            System.out.println("当前id"+(Long)req.getSession().getAttribute("employee"));
            BaseContext.setid((Long)req.getSession().getAttribute("employee"));
            System.out.println(BaseContext.getid());
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        //判断是否用户登录
        if(req.getSession().getAttribute("user")!=null){
            System.out.println("当前id"+(Long)req.getSession().getAttribute("user"));
            BaseContext.setid((Long)req.getSession().getAttribute("user"));
            System.out.println(BaseContext.getid());
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        //未登录，通过输出流返回json数据
        System.out.println("被拦截"+uri);
        resp.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
}