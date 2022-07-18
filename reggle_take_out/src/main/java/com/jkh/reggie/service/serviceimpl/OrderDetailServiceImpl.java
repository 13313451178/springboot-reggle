package com.jkh.reggie.service.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jkh.reggie.entity.OrderDetail;
import com.jkh.reggie.mapper.OrderDetailMapper;
import com.jkh.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
