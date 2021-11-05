package com.tanhua.server.controller;


import com.tanhua.domain.db.User;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.server.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 1,请求方式 ：post
     * 2，请求参数：json 格式
     * 3,@RequestBody : 把请求的json 数据转换为javaBean
     * 4,post请求  访问地址 http:localhost:18080/user
     * @param user
     * @return
     */
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody User user){
        return userService.save(user);
    }

    /**
     * 1，get请求  访问地址 http:localhost:18080/user/findByMobile?mobile=xxxx
     * @param mobile
     * @return
     */
    @GetMapping("findByMobile")//findByMobile 也可以省略
    public ResponseEntity<Object> findByMobile(String mobile){

        return userService.findByMobile(mobile);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String,String> paramMap){
        return userService.login(paramMap);
    }
    //短信验证码
    @PostMapping("/loginVerification")
    public ResponseEntity<Object> loginVerification(@RequestBody Map<String,String> paramMap){
        return userService.loginVerification(paramMap);
    }


    /*
    @RequestHeader 相当于：
        request.getHeader("Authorization")
     */
    @PostMapping("/loginReginfo")
    public ResponseEntity<Object> loginReginfo(
            @RequestBody UserInfo userInfo){
        return userService.loginReginfo(userInfo);
    }

    /*
    /user/loginReginfo/head  修改头像
    MultipartFile springmvc 中用来封装上传文件的对象
     */
    @PostMapping("/loginReginfo/head")
    public ResponseEntity<Object> updateHead(MultipartFile headPhoto) throws IOException {
        return userService.updateHead(headPhoto);

    }

    /*public static void main(String[] args) {
        System.out.println(Math.random());
    }*/
}
