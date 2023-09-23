package com.itheima.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie_take_out.dto.DishDto;
import com.itheima.reggie_take_out.mapper.DishMapper;
import com.itheima.reggie_take_out.pojo.Dish;
import com.itheima.reggie_take_out.pojo.DishFlavor;
import com.itheima.reggie_take_out.service.DishFlavorService;
import com.itheima.reggie_take_out.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional//开启事务
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本信息到菜品表
        this.save(dishDto);
        //保存菜品口味信息到菜品口味表
        Long dishid = dishDto.getId();//菜品id
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.forEach(item->{
            item.setDishId(dishid);
        });                        //设置菜品id
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishdto) {
        //更新菜品基本信息到菜品表
        this.updateById(dishdto);
        //删除菜品口味信息到菜品口味表
        Long dishid = dishdto.getId();//菜品id
        LambdaQueryWrapper<DishFlavor> lw = new LambdaQueryWrapper<>();
        lw.eq(DishFlavor::getDishId,dishid);
        dishFlavorService.remove(lw);
        //更新菜品口味信息到菜品口味表
        List<DishFlavor> flavors = dishdto.getFlavors();
        flavors.forEach(item->{
            item.setDishId(dishid);
        });                        //设置菜品id
        dishFlavorService.saveBatch(flavors);
    }
}
