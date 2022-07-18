package com.jkh.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

/*交给spring容器*/
@Component
@Slf4j
/*mybatis-plus 提供的公共字段自动填冲*/
public class MyMateObjectHandle implements MetaObjectHandler {

    @Override
    /*插入操作自动填充*/
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充"+metaObject.toString());
        /*设置默认填充字段*/
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());

    }

    @Override
    /*更新操作自动填充*/
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充"+metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
