package com.jkh.reggie.filter;

;
import com.alibaba.fastjson.JSON;
import com.jkh.reggie.common.BaseContext;
import com.jkh.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*完善登录功能 在进入tomcat前加过滤器判断是否登录*/
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
/*业务逻辑
 * 1.获取本次请求的uri
 * 2.判断请求的URI是否需要处理
 * 3.不需要处理 直接放行
 * 4判断登录状态 已登录 放行
 * */
public class loginCheckFilter implements Filter {
    /*路径匹配器 */
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        log.info("拦截的请求"+requestURI);
        String[] urls = new String[]{
                /*登录请求不用检测*/
                "/employee/login",
                /*退出请求不用检测*/
                "/employee/login",
                /*静态页面不用检测*/
                "/backend/**",
                "/front/**",
                "/user/login",
                "/user/sendMsg"


        };
        /*2.判断请求的URI是否需要处理
        * 调用封装的方法*/
        boolean check = check(requestURI, urls);
        if(check){
            log.info("本次请求不需要处理");
            filterChain.doFilter(request,response);
            return;
        }
            /*4判断登录状态 已登录 放行*/
        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录");
            /*用ThreadLocal来获取登录用户id*/
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            log.info("用户id为"+empId);
            filterChain.doFilter(request,response);
            return;
        }
        /*判断移动短用户登录状态*/
        if(request.getSession().getAttribute("user")!=null){
            log.info("用户已登录");
            /*用ThreadLocal来获取登录用户id*/
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            log.info("用户id为"+userId);
            filterChain.doFilter(request,response);
            return;
        }

        /*如果未登录返回登录页面 通过输出流的方式回写客户端一个R对象*/
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
    /*封装一个路径匹配判断方法*/
    public boolean check(String requestURI,String[] urls){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;

    }
}
