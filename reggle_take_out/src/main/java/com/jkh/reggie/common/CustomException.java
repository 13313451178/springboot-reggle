package com.jkh.reggie.common;
/*在删除分类时自定义业务异常 若该分类关联了菜品或套餐则抛出异常*/
public class CustomException extends RuntimeException{

    public CustomException(String message) {
        super(message);
    }
}
