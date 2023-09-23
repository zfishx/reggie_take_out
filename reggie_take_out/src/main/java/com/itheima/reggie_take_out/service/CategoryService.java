package com.itheima.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie_take_out.pojo.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
