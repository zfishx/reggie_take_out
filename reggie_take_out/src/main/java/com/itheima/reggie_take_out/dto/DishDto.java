package com.itheima.reggie_take_out.dto;

import com.itheima.reggie_take_out.pojo.Dish;
import com.itheima.reggie_take_out.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
