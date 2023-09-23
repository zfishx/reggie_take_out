package com.itheima.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie_take_out.pojo.ShoppingCart;
import com.itheima.reggie_take_out.service.ShoppingCartService;
import com.itheima.reggie_take_out.utils.BaseContext;
import com.itheima.reggie_take_out.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getid());//设置购物车用户id
        //查询购物车中是否有该商品，如果有，数量加一，如果没有，新增
        LambdaQueryWrapper<ShoppingCart> lw = new LambdaQueryWrapper<>();
        lw.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        if(shoppingCart.getDishId() == null){//说明是套餐
            lw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        else{
            lw.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
            //lw.eq(ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor());
        }
        ShoppingCart sc = shoppingCartService.getOne(lw);
        if(sc == null){
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            return R.success(shoppingCart);
        }
        else{
            int num = sc.getNumber();
            sc.setNumber(num+1);
            shoppingCartService.updateById(sc);
            return R.success(sc);
        }
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        Long userid = BaseContext.getid();
        LambdaQueryWrapper<ShoppingCart> lw = new LambdaQueryWrapper<>();
        lw.eq(ShoppingCart::getUserId,userid);
        lw.orderByAsc(ShoppingCart::getCreateTime);//按照创建时间升序排列(先加入的先显示
        List<ShoppingCart> list = shoppingCartService.list(lw);
        return R.success(list);
    }
    @DeleteMapping("clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> lw = new LambdaQueryWrapper<>();
        lw.eq(ShoppingCart::getUserId,BaseContext.getid());
        shoppingCartService.remove(lw);
        return R.success("清空购物车成功");
    }
}
