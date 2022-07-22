package com.jkh.reggie.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jkh.reggie.common.BaseContext;
import com.jkh.reggie.common.CustomException;
import com.jkh.reggie.entity.*;
import com.jkh.reggie.mapper.OrdersMapper;
import com.jkh.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>  implements OrdersService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Transactional
    @Override
    /*生成订单*/
    public void submit(Orders orders) {
        /*获取当前用户id*/
        Long currentId = BaseContext.getCurrentId();
        /*查询当前用户购物车id*/
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        if(list==null||list.size()==0){
            throw new CustomException("购物车为空");
        }
        /*查询用户数据和地址数据*/
        User user = userService.getById(currentId);
        /*查询地址*/
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook==null){
            throw new CustomException("地址信息有误");
        }
        /*向订单表插入一条数据*/
        long id = IdWorker.getId();
        /*遍历购物车数据*/
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail>orderDetails=list.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(id);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;


        }).collect(Collectors.toList());
        orders.setId(id);
        orders.setNumber(String.valueOf(id));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(currentId);
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        this.save(orders);
        /*向订单明细标配插入数据*/
        orderDetailService.saveBatch(orderDetails);
        /*清空购物车*/
        shoppingCartService.remove(queryWrapper);
    }
}
