package com.itheima.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie_take_out.pojo.Category;
import com.itheima.reggie_take_out.service.CategoryService;
import com.itheima.reggie_take_out.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //新增分类
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        Page<Category> categoryPage = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort排序
        qw.orderByAsc(Category::getSort);
        categoryService.page(categoryPage,qw);
        return R.success(categoryPage);
    }
    @DeleteMapping
    public R<String> delete(Long ids){
        //categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("删除分类成功");
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.eq(category.getType()!=null,Category::getType,category.getType());
        qw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(qw);
        return R.success(list);
    }
}
