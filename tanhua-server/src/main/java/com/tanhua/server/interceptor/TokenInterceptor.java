package com.tanhua.server.interceptor;

import com.tanhua.domain.db.User;
import com.tanhua.server.service.UserService;
import com.tanhua.server.utils.UserHolder;
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
    private UserService userService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        log.info("请求的ip：{},请求的路径：{}",request.getRemoteHost(),request.getRequestURL());
        //1,获取token
        String token = request.getHeader("Authorization");
        if (token == null){
            response.setStatus(401);
            return false;
        }
        //2,验证user 是否合法
        User user = userService.findByToken(token);
        if (user == null){
            response.setStatus(401);
            return false;
        }
        //3，将user存入 当前线程，方便后面调用
        UserHolder.set(user);
        return true;
    }

}
