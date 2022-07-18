package com.jkh.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jkh.reggie.Dto.DishDto;
import com.jkh.reggie.Dto.SetmealDto;
import com.jkh.reggie.common.R;
import com.jkh.reggie.entity.Category;
import com.jkh.reggie.entity.Setmeal;
import com.jkh.reggie.service.CategoryService;
import com.jkh.reggie.service.SetmealDishService;
import com.jkh.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Lang;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("setmealDto"+setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }
    /*分页查询*/
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name){
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        /*由于setmeal中没有套餐分类名 建立一个包含套餐分类的实体类*/
        Page<SetmealDto> setmealDtoPage=new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        queryWrapper.like(name!=null,Setmeal::getName,name);
        setmealService.page(setmealPage, queryWrapper);
        /*把属性赋值到setmealdto中*/
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        /*获取每个套餐的实体类*/
        List<Setmeal> records = setmealPage.getRecords();
        ArrayList<SetmealDto> setmealDtos = new ArrayList<>();
        /*获取实体类的种类id*/

        for (Setmeal record : records) {
            /*创建一个SetmealDto*/
            SetmealDto setmealDto = new SetmealDto();
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                setmealDto.setCategoryName(category.getName());

            }

            /*把其他属性赋值到setmealdto中*/
            BeanUtils.copyProperties(record,setmealDto);
            setmealDtos.add(setmealDto);
        }
        setmealDtoPage.setRecords(setmealDtos);
        return R.success(setmealDtoPage);
    }
    /*套餐停售*/
    @PostMapping("/status/{status}")
    public R<String>  status(@PathVariable Integer status,@RequestParam List<Long> ids){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        List<Setmeal> list = setmealService.list(queryWrapper);
        for (Setmeal setmeal : list) {
            if(setmeal!=null){
                setmeal.setStatus(status);
                setmealService.updateById(setmeal);
            }
        }
        return R.success("状态修改成功");
    }
    /*删除*/
    @DeleteMapping
    public R<String > delete(@RequestParam List<Long> ids){
        setmealService.deleteByIds(ids);
        return R.success("删除成功");
    }
//    在移动端展示
    @GetMapping("/list")
    public R<List<Setmeal>> list(Long categoryId,int status){
        log.info("categoryId="+categoryId+"status="+status);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,categoryId);
        queryWrapper.eq(Setmeal::getStatus,status);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
}
