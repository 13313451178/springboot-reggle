package com.jkh.reggie.service.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jkh.reggie.entity.Employee;
import com.jkh.reggie.mapper.EmployeeMapper;
import com.jkh.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
