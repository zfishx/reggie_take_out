package com.itheima.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie_take_out.mapper.CategoryMapper;
import com.itheima.reggie_take_out.pojo.Category;
import com.itheima.reggie_take_out.pojo.Dish;
import com.itheima.reggie_take_out.pojo.Setmeal;
import com.itheima.reggie_take_out.service.CategoryService;
import com.itheima.reggie_take_out.service.DishService;
import com.itheima.reggie_take_out.service.SetmealService;
import com.itheima.reggie_take_out.utils.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishservice;
    @Autowired
    private SetmealService setmealservice;
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联菜品，如果关联则不能删除，抛出业务异常
        LambdaQueryWrapper<Dish> lw1= new LambdaQueryWrapper<>();
        lw1.eq(Dish::getCategoryId,id);
        int count = dishservice.count(lw1);
        if(count > 0){
            throw new CustomException("当前分类关联菜品，不能删除");
        }
        //查询当前分类是否关联套餐，如果关联则不能删除，抛出业务异常
        LambdaQueryWrapper<Setmeal> lw2 = new LambdaQueryWrapper<>();
        lw2.eq(Setmeal::getCategoryId,id);
        int count2 = setmealservice.count(lw2);
        if(count2 > 0){
            throw new CustomException("当前分类关联套餐，不能删除");
        }
        //正常删除
        super.removeById(id);
    }
}
