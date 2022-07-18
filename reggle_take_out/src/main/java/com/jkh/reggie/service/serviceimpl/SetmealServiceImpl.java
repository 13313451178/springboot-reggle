package com.jkh.reggie.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jkh.reggie.Dto.SetmealDto;
import com.jkh.reggie.common.CustomException;
import com.jkh.reggie.entity.Setmeal;
import com.jkh.reggie.entity.SetmealDish;
import com.jkh.reggie.mapper.SetmealMapper;
import com.jkh.reggie.service.SetmealDishService;
import com.jkh.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        /*保存菜单基本信息*/
        this.save(setmealDto);
        /*保存关联对象*/
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);
    }
    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        /*在删除之前要判断当前是否是停售状态
        * 删除套餐时删除和菜品的关联数据*/
        /*判断是否停售*/
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.in(Setmeal::getId,ids);
        setmealQueryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(setmealQueryWrapper);
        if(count>0){
            /*有在出售的套餐 不能删除 抛出异常*/
            throw  new CustomException("套餐正在售卖");
        }
        /*删除套餐数据*/
        this.removeByIds(ids);
        /*删除套餐 菜品关联数据*/
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper);


    }
}
