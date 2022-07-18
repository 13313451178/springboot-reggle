package com.jkh.reggie.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jkh.reggie.common.CustomException;
import com.jkh.reggie.entity.Category;
import com.jkh.reggie.entity.Dish;
import com.jkh.reggie.entity.Setmeal;
import com.jkh.reggie.mapper.CategoryMapper;
import com.jkh.reggie.service.CategoryService;
import com.jkh.reggie.service.DishService;
import com.jkh.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /*根据id删除分类 在删除之前判断是否关联其它菜品或菜单*/
    @Override
    public void remove(Long id) {
        /*判断是否关联菜品*/
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        /*根据分类id查询*/
        dishQueryWrapper.eq(Dish::getCategoryId,id);
        int dishCount = dishService.count(dishQueryWrapper);
        if(dishCount>0){
//            关联了菜品 抛出一个自定义业务异常
            throw new CustomException("当前分类关联了菜品，请先移除菜品");

        }
        /*判断关联套餐*/
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper= new LambdaQueryWrapper<>();
        /*根据分类id查询*/
        setmealQueryWrapper.eq(Setmeal::getCategoryId,id);
        int setmealCount = setmealService.count(setmealQueryWrapper);
        if(setmealCount>0){
//            关联了套餐 抛出一个全局异常
            throw new CustomException("当前分类关联了套餐，请先移除套餐");
        }
        super.removeById(id);
    }
}
