package com.tanhua.manage.interceptor;

import com.tanhua.domain.db.User;
import com.tanhua.manage.Utils.AdminHolder;
import com.tanhua.manage.pojo.Admin;
import com.tanhua.manage.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//token 统一处理的拦截器
@Component
@Slf4j //功能打印 log.info
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminService adminService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        log.info("===拦截器，请求的ip：{},请求的路径：{}",request.getRemoteHost(),request.getRequestURL());
        //1,获取token
        String token = request.getHeader("Authorization");
        if (token == null){
            response.setStatus(401);
            return false;
        }else {
            //2,验证user 是否合法
            Admin admin = adminService.findByToken(token);
            if (admin == null){
                response.setStatus(401);
                return false;
            }
            //3，将user存入 当前线程，方便后面调用
            AdminHolder.set(admin);
        }


        return true;
    }

}
