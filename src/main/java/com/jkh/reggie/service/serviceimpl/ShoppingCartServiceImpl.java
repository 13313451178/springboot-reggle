package com.jkh.reggie.service.serviceimpl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jkh.reggie.entity.ShoppingCart;
import com.jkh.reggie.mapper.ShoppingCartMapper;
import com.jkh.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper,ShoppingCart> implements ShoppingCartService {


}
