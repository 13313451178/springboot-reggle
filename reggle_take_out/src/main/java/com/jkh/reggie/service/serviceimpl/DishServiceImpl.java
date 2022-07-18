package com.jkh.reggie.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jkh.reggie.Dto.DishDto;
import com.jkh.reggie.common.CustomException;
import com.jkh.reggie.entity.Dish;
import com.jkh.reggie.entity.DishFlavor;
import com.jkh.reggie.mapper.DishMapper;
import com.jkh.reggie.service.DishFlavorService;
import com.jkh.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Transactional
    @Override
    public void saveDishAndFlavor(DishDto dishDto) {
        /*保存彩屏基本信息到基本表*/
        this.save(dishDto);
        /*获取菜品id*/
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }
        /*用流的方法处理*/
       /* flavors=flavors.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());*/
//        保存菜品口味
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public DishDto getDishAndDishFlavorById(Long id) {
        DishDto dishDto = new DishDto();
        /*根据id查询Dish的基本信息*/
        Dish dish = this.getById(id);
        /*把基本属性拷贝到dishdto中*/
        BeanUtils.copyProperties(dish,dishDto);
        /*查询口味信息*/
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        /*给dishdto设置口味属性*/
        dishDto.setFlavors(list);

        return dishDto;
    }

    @Transactional
    @Override
    public void updateDishAndDishFlavor(DishDto dishDto) {
        /*更新基础数据*/
        this.updateById(dishDto);

        /*先清理掉口味信息*/
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
          /*在添加口味*/
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(flavors);
    }

    @Transactional
    @Override
    public void deleteDishByIds(List<Long> ids) {
        /*删除功能同时删除菜品和口味*/
        /*首先要判断当前菜品是否在售*/
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.in(ids!=null,Dish::getId,ids);
        /*查询被选中菜品的dish对象*/
        List<Dish> list = this.list(dishQueryWrapper);
        for (Dish dish : list) {
            /*如果菜品停售才能删除*/
            if(dish.getStatus()==0){
                this.removeById(dish.getId());
            }else {
                /*如果菜品正在售卖抛出异常*/
                throw  new CustomException("当前菜品正在售卖");
            }
        }

    }

}
