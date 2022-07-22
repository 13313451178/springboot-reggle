package com.jkh.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jkh.reggie.Dto.DishDto;
import com.jkh.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    /*新增菜品同时添加口味*/
    public void saveDishAndFlavor(DishDto dishDto);
    public DishDto getDishAndDishFlavorById(Long id);
    public void updateDishAndDishFlavor(DishDto dishDto);
    /*菜品的删除*/
    public void deleteDishByIds(List<Long> ids);
}
