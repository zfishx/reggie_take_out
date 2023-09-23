package com.itheima.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie_take_out.dto.DishDto;
import com.itheima.reggie_take_out.pojo.Category;
import com.itheima.reggie_take_out.pojo.Dish;
import com.itheima.reggie_take_out.pojo.DishFlavor;
import com.itheima.reggie_take_out.service.CategoryService;
import com.itheima.reggie_take_out.service.DishFlavorService;
import com.itheima.reggie_take_out.service.DishService;
import com.itheima.reggie_take_out.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//菜品管理
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishservice;

    @Autowired
    private DishFlavorService dishflavorservice;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishdto){
        dishservice.saveWithFlavor(dishdto);
        return R.success("新增菜品成功");
    }
    //菜品信息分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name){
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.like(name!=null,Dish::getName,name);
        qw.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishPage = dishservice.page(dishPage,qw);
        //对象拷贝
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        List <Dish> records = dishPage.getRecords();
        List <DishDto> list = new ArrayList<>();
        records.forEach(item->{
            Long categoryid = item.getCategoryId();//分类id
            Category c = categoryService.getById(categoryid);//分类信息
            String name2 = c.getName();//分类名称
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            dishDto.setCategoryName(name2);
            list.add(dishDto);
        });
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }
    //根据id查询菜品信息和口味信息
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        Dish d = dishservice.getById(id);
        LambdaQueryWrapper<DishFlavor> lw = new LambdaQueryWrapper<>();
        lw.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dfs = dishflavorservice.list(lw);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(d,dishDto);
        dishDto.setFlavors(dfs);
        return R.success(dishDto);
    }

    //修改菜品信息
    @PutMapping
    public R<String> update(@RequestBody DishDto dishdto){
        dishservice.updateWithFlavor(dishdto);
        return R.success("新增菜品成功");
    }
    /*@GetMapping("/list")
    //根据菜品分类id查询菜品信息
    public R<List<Dish>> list(Dish dish){//使用dish而不是categoryid是为了通用性强
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        qw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        qw.eq(Dish::getStatus,1);//在售
        List<Dish> list = dishservice.list(qw);
        return R.success(list);

    }*/
    @GetMapping("/list")
    //根据菜品分类id查询菜品信息
    public R<List<DishDto>> list(Dish dish){//使用dish而不是categoryid是为了通用性强
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        qw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        qw.eq(Dish::getStatus,1);//在售
        List<Dish> list = dishservice.list(qw);
        List<DishDto> list2 = new ArrayList<>();
        list.forEach(item->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            LambdaQueryWrapper<DishFlavor> lw = new LambdaQueryWrapper<>();
            lw.eq(DishFlavor::getDishId,item.getId());
            List<DishFlavor> dfs = dishflavorservice.list(lw);
            dishDto.setFlavors(dfs);
            list2.add(dishDto);
        });
        return R.success(list2);

    }


}
