package com.itheima.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie_take_out.dto.DishDto;
import com.itheima.reggie_take_out.pojo.Dish;

public interface DishService extends IService<Dish> {
    //保存菜品信息和口味信息，同时修改dish和dish_flavor表
    public void saveWithFlavor(DishDto dishDto);

    //更新菜品信息和口味信息，同时修改dish和dish_flavor表
    public void updateWithFlavor(DishDto dishdto);
}
