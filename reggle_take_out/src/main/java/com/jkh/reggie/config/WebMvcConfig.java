package com.jkh.reggie.config;
/*Spring Boot 对静态资源映射提供了默认配置， 默认将 /** 所有访问映射到以下目录：

classpath:/static
classpath:/public
classpath:/resources
classpath:/META-INF/resources
自己配置访问路径*/

import com.jkh.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
/*设置静态资源映射*/
@Slf4j
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /*扩张mvc消息装换器 在页面发生update请求时 主键id使用雪花算法lang型数据会使数据失真 工自定义消息装换器*/
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        /*将结果装换为json数据返回前端页面*/
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        /*将新增的转换器容器加到mvc框架装换器集合*/
        converters.add(0,messageConverter);
    }

}
