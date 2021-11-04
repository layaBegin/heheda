package com.tanhua.server.controller;

import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserInfoController {


    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Object> findUserInfo(){

        return userService.findUserInfoById();
    }

    //@RequestBody 将客户端传过来的字段转换为UserInfo对象
    @PutMapping
    public ResponseEntity<Object> updateUserInfo(@RequestBody UserInfo userInfo){

        return userService.updateUserInfo(userInfo);
    }

}