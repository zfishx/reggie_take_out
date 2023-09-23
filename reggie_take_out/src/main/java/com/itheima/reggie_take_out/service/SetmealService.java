package com.itheima.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie_take_out.dto.SetmealDto;
import com.itheima.reggie_take_out.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void savewithdish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
