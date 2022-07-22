package com.jkh.reggie;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.transaction.annotation.EnableTransactionManagement;


/*lombok提供日志*/
@Slf4j
@SpringBootApplication
/*扫描控制器*/
@ServletComponentScan
@EnableTransactionManagement
/*开启springcach注解缓存*/
@EnableCaching

public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动");
    }
}
