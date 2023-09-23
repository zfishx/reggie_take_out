package com.itheima.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie_take_out.dto.SetmealDto;
import com.itheima.reggie_take_out.mapper.SetmealMapper;
import com.itheima.reggie_take_out.pojo.Category;
import com.itheima.reggie_take_out.pojo.Setmeal;
import com.itheima.reggie_take_out.pojo.SetmealDish;
import com.itheima.reggie_take_out.service.CategoryService;
import com.itheima.reggie_take_out.service.SetmealDishService;
import com.itheima.reggie_take_out.service.SetmealService;
import com.itheima.reggie_take_out.utils.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Override
    @Transactional
    public void savewithdish(SetmealDto setmealDto) {
       /* String categoryName = setmealDto.getCategoryName();
        LambdaQueryWrapper<Category> lw = new LambdaQueryWrapper<>();
        lw.eq(Category::getName,categoryName);
        Category category = categoryService.getOne(lw);
        if(category == null){
            log.info(categoryName);
            throw new RuntimeException("套餐分类不存在");
        }*/
        //setmealDto.setCategoryId(category.getId());
        log.info(String.valueOf(setmealDto.getCategoryId()));
        this.save(setmealDto);//保存套餐基本信息到套餐表
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(item->{
            item.setSetmealId(setmealDto.getId());
        });
        //保存套餐菜品信息到套餐菜品表
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lw = new LambdaQueryWrapper<>();
        lw.in(Setmeal::getId,ids);
        lw.eq(Setmeal::getStatus,1);
        //查询套餐状态，确定是否可用后再删除
        int count = this.count(lw);
        if(count>0){
            throw new CustomException("套餐已上架，不能删除");
        }
        //删除套餐基本信息到套餐表
        this.removeByIds(ids);
        //删除套餐菜品信息到套餐菜品表
        LambdaQueryWrapper<SetmealDish> lw2 = new LambdaQueryWrapper<>();
        lw2.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lw2);
    }
}
