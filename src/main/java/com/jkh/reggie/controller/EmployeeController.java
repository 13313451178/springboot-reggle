package com.jkh.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jkh.reggie.common.R;
import com.jkh.reggie.entity.Employee;
import com.jkh.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /*登录功能逻辑实现*/
    /*1.将页面提交的密码进行md5加密处理
    * 2.根据页面提交对的用户名进行数据库查询
    * 3.没有查询到返回登录失败结果
    * 4.密码比对 比较不一致返回失败
    * 5.检查账号状态是否被锁定
    * 6.登陆成功将员工id存入session*/
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        /*要求1*/
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        /*查询数据库*/
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        if(emp==null){
            return R.error("查无该用户");
        }
        if(!emp.getPassword().equals(password)){
            return R.error("密码不正确");
        }
        /*查看员工状态*/
        if(emp.getStatus()==0){
            return R.error("员工账户被冻结");
        }
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    @RequestMapping ("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    /*添加员工信息*/
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工"+employee.toString());
        /*设置初始密码 123456进行md5加密*/
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        /*employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());*/
//        获取当前用户id
        long empid = (long) request.getSession().getAttribute("employee");
    /*    employee.setCreateUser(empid);
        employee.setUpdateUser(empid);*/
        employeeService.save(employee);
        return R.success("新增员工成功");
    }
    /*分页*/
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
//        构造分页构造器
        Page pageinfo = new Page(page, pageSize);
        /*构造条件构造器*/
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        /*添加过滤条件*/
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        /*添加排序条件*/
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        /*执行查询*/
        employeeService.page(pageinfo,queryWrapper);

        return R.success(pageinfo);
    }
    /*根据id 修改员工信息*/
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){

        log.info(employee.toString());
        Long empId = (Long) request.getSession().getAttribute("employee");
       /* employee.setUpdateUser(empId);
        employee.setUpdateTime(LocalDateTime.now());*/
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return R.error("系统出错，请稍后再试");

    }

}
