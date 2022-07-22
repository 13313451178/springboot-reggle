package com.jkh.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jkh.reggie.Dto.SetmealDto;
import com.jkh.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /*新增套餐  同时保存菜品套餐关联关系*/
    public  void saveWithDish(SetmealDto setmealDto);
    /*删除套餐 判断是否停售*/
    public void deleteByIds(List<Long> ids);
}
