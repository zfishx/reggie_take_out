package com.itheima.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie_take_out.dto.OrderDto;
import com.itheima.reggie_take_out.pojo.OrderDetail;
import com.itheima.reggie_take_out.pojo.Orders;
import com.itheima.reggie_take_out.service.OrderDetailService;
import com.itheima.reggie_take_out.service.OrderService;
import com.itheima.reggie_take_out.utils.BaseContext;
import com.itheima.reggie_take_out.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }
    @GetMapping("/userPage")
    public R<Page> orderpage(int page,int pageSize){
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> lw = new LambdaQueryWrapper<>();
        Long userid = BaseContext.getid();
        lw.eq(Orders::getUserId,userid);
        ordersPage = orderService.page(ordersPage,lw);
        Page<OrderDto> orderDtoPage = new Page<>(page,pageSize);
        BeanUtils.copyProperties(ordersPage,orderDtoPage,"records");
        List<OrderDto> records = new ArrayList<>();
        ordersPage.getRecords().forEach(item->{
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item,orderDto);
            LambdaQueryWrapper<OrderDetail> lw2 = new LambdaQueryWrapper<>();
            lw2.eq(OrderDetail::getOrderId,item.getId());
            List<OrderDetail> ods = orderDetailService.list(lw2);
            orderDto.setOrderDetails(ods);
            records.add(orderDto);
        });
        orderDtoPage.setRecords(records);
        return R.success(orderDtoPage);
    }
}
