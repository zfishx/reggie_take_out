package com.itheima.reggie_take_out.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie_take_out.pojo.Orders;

public interface OrderService extends IService<Orders>{
    void submit(Orders orders);
}
