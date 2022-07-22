package com.jkh.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jkh.reggie.common.R;
import com.jkh.reggie.entity.Category;
import com.jkh.reggie.entity.Employee;
import com.jkh.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("catecary"+category);
        categoryService.save(category);
        return R.success("新建分类成功");
    }
    @GetMapping("/page")
    public R<Page>page (int page,int pageSize){
        Page<Category> pageInfo = new Page<>(page, pageSize);
        log.info("pageInfo"+pageInfo.toString());
        /*构造条件构造器*/
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        /*添加排序条件*/
        queryWrapper.orderByAsc(Category::getSort);
        /*执行查询*/
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    /*根据id删除分类*/
    @DeleteMapping
    public R<String> delete(Long ids){
        /*categoryService.removeById(id);*/
        /*调用自己写的业务逻辑*/
        log.info("id"+ids);
        categoryService.remove(ids);
        return R.success("分类删除成功");

    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }
    /*添加菜品是菜品种类的数据字典 参数可以是type 也可实体类*/
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort);
        queryWrapper.orderByDesc(Category::getCreateTime);
        List<Category> list = categoryService.list(queryWrapper);
        /*当参数是type时*/
//        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Category::getType,type);
//        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);

    }
}
