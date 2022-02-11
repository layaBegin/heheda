package com.tanhua.manage.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.tanhua.manage.Utils.AdminHolder;
import com.tanhua.manage.pojo.Admin;
import com.tanhua.manage.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/system/users")
public class AdminController {

    @Autowired
    private AdminService adminService;


    //生成图片验证码
    @GetMapping("/verification")
    public void saveVerification(HttpServletResponse response,String uuid) throws IOException {
        //2、调用hutool工具类，生成验证码图片
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(299, 97);
        //3、获取验证码文字
        String code = circleCaptcha.getCode();
        System.out.println("==code:" + code);
        //4、存入redis
        adminService.saveCap(uuid,code);
        //5、写入响应流
        circleCaptcha.write(response.getOutputStream());
    }

    ///system/users/login
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String,String> paramsMap) throws Exception {

        String username = paramsMap.get("username");
        String password = paramsMap.get("password");
        String verificationCode = paramsMap.get("verificationCode");
        String uuid = paramsMap.get("uuid");
        return adminService.login(username,password,verificationCode,uuid);

    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String token){
        token = token.replace("Bearer ","");

        return adminService.logout(token);
    }

    //system/users/profile
    @PostMapping("/profile")
    public ResponseEntity<Object> getProfile(){
        return adminService.getProfile();
    }

    @GetMapping("/dashboard/summary")
    public ResponseEntity<Object> getSummary(@RequestHeader("Authorization") String token){
        Admin admin = AdminHolder.get();

        return null;
    }
}
