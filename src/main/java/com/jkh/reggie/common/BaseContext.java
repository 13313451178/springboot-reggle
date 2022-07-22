package com.jkh.reggie.common;

import org.apache.ibatis.annotations.Lang;

/*保存ThreadLocal封装工具类*/
public class BaseContext {
    private static  ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);

    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
