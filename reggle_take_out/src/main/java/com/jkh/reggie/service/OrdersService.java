package com.jkh.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jkh.reggie.entity.Orders;

public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);
}
