package com.jkh.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jkh.reggie.common.BaseContext;
import com.jkh.reggie.common.R;
import com.jkh.reggie.entity.Orders;
import com.jkh.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("生成订单");

    }
    /*查看订单*/
    @GetMapping("/userPage")
    public R<Page> page(int page,int pageSize){
        Page<Orders> orddersPage = new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        ordersService.page(orddersPage,queryWrapper);
        return R.success(orddersPage);
    }
}
