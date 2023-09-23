package com.itheima.reggie_take_out.dto;

import com.itheima.reggie_take_out.pojo.OrderDetail;
import com.itheima.reggie_take_out.pojo.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {
    private List<OrderDetail> orderDetails;
}
