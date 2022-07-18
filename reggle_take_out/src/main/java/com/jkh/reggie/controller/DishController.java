package com.jkh.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jkh.reggie.Dto.DishDto;

import com.jkh.reggie.common.BaseContext;
import com.jkh.reggie.common.R;
import com.jkh.reggie.entity.Category;
import com.jkh.reggie.entity.Dish;
import com.jkh.reggie.entity.DishFlavor;

import com.jkh.reggie.service.CategoryService;
import com.jkh.reggie.service.DishFlavorService;
import com.jkh.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    /*新增菜品*/
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveDishAndFlavor(dishDto);
        return R.success("新增菜品成功");
    }
    /*分页*/
    @GetMapping("/page")
    public R<Page>page(int page,int pageSize,String name){
        Page<Dish> pageInfo = new Page(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);
        /*对象拷贝 把pageinfo中的数据拷贝到dishDtopage中 拷贝处数据之外的属性*/
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        /*拿到pageinfo中的属性*/
        List<Dish> records = pageInfo.getRecords();
        ArrayList<DishDto> dishDtoList = new ArrayList<>();
        for (Dish record : records) {
//            创建一个DishDto容器
            DishDto dishDto = new DishDto();
            /*闹到菜品种类的id*/
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                /*获取分类名称*/
                String categoryName = category.getName();
                /*把名字设置到容器中*/
                dishDto.setCategoryName(categoryName);
            }
//
            /*把其他属性赋值到容器中*/
            BeanUtils.copyProperties(record,dishDto);
            dishDtoList.add(dishDto);
        }
        List<DishDto> list=dishDtoList;
        /*设置DishDto中records属性*/
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);

    }
    /*修改菜品*/
    @GetMapping ("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getDishAndDishFlavorById(id);
        return R.success(dishDto);
    }
    /*修改菜品*/
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateDishAndDishFlavor(dishDto);
        return R.success("修改菜品成功");
    }
    /*菜品的停售起售*/
/*    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status,Long ids){
        log.info("status"+status);
        log.info("ids"+ids);
        Dish dish = dishService.getById(ids);
        if(dish!=null){
            dish.setStatus(status);
            *//*更新到数据库*//*
            dishService.updateById(dish);
            return R.success("开始起售");
        }
        return R.error("售卖状态设置异常");
    }*/
    /*改进批量起售 停售*/
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status, @RequestParam List<Long> ids){
        log.info("status"+status);
        log.info("ids "+ids);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        List<Dish> list = dishService.list(queryWrapper);
        for (Dish dish : list) {
            if(dish!=null){
                dish.setStatus(status);
                dishService.updateById(dish);
            }
        }
        return R.success("状态修改成功");


    }

    /*菜品删除*/
   /* @DeleteMapping
    public R<String> delete(long ids){
        log.info("ids"+ids);
        dishService.removeById(ids);
        dishService.deleteDishAndDishFlavor(ids);
        return R.success("删除成功");
    }*/
    /*批量删除*/
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:"+ids);
        dishService.deleteDishByIds(ids);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(queryWrapper);
        return R.success("删除成功");
    }
//    在添加套餐表单中获取菜品表单
   /* @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        log.info("dish"+dish.toString());
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        if(dish.getName()!=null){
            queryWrapper.or().like(Dish::getName,dish.getName());
        }
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }*/
    /**/
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        log.info("dish"+dish.toString());
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        if(dish.getName()!=null){
           queryWrapper.or().like(Dish::getName,dish.getName());
        }
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        ArrayList<DishDto> arrayList = new ArrayList<>();
        for (Dish dish1 : list) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1,dishDto);
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dish1.getId());
            List<DishFlavor> list1 = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(list1);
            arrayList.add(dishDto);
        }
        List<DishDto> dishDtolist=arrayList;
        return R.success(dishDtolist);
}





}
