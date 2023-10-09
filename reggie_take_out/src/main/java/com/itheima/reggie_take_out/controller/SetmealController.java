package com.itheima.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie_take_out.dto.SetmealDto;
import com.itheima.reggie_take_out.pojo.Setmeal;
import com.itheima.reggie_take_out.service.CategoryService;
import com.itheima.reggie_take_out.service.SetmealDishService;
import com.itheima.reggie_take_out.service.SetmealService;
import com.itheima.reggie_take_out.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//套餐管理
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.savewithdish(setmealDto);
        return R.success("新增套餐成功");
    }
    //套餐分页查询
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> lw = new LambdaQueryWrapper<>();
        lw.like(name!=null,Setmeal::getName,name);
        lw.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage,lw);
        Page<SetmealDto> setmealDtoPage = new Page<>(page,pageSize);
        //把setmealPage的数据拷贝到setmealDtoPage
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<SetmealDto> records = new ArrayList<>();
        setmealPage.getRecords().forEach(item->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            String cname = categoryService.getById(categoryId).getName();
            setmealDto.setCategoryName(cname);
            records.add(setmealDto);
        });
        setmealDtoPage.setRecords(records);
        return R.success(setmealDtoPage);
    }
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("删除套餐成功");
    }
    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#categoryId+'_'+#status",unless = "#result==null")
    public R<List<Setmeal>> list(Long categoryId,int status){
        LambdaQueryWrapper<Setmeal> lw = new LambdaQueryWrapper<>();
        lw.eq(categoryId!=null,Setmeal::getCategoryId,categoryId);
        lw.eq(Setmeal::getStatus,status);
        List<Setmeal> list = setmealService.list(lw);
        return R.success(list);
    }
}
