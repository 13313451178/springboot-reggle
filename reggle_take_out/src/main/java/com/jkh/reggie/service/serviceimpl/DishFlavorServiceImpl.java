package com.jkh.reggie.service.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jkh.reggie.entity.DishFlavor;
import com.jkh.reggie.mapper.DishFlavorMapper;
import com.jkh.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
