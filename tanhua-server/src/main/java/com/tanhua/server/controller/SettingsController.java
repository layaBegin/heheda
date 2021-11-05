package com.tanhua.server.controller;

import com.tanhua.domain.db.Settings;
import com.tanhua.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("users")
@Slf4j
public class SettingsController {

    @Autowired
    private UserService userService;

    //查询通知设置
    @GetMapping("settings")
    public ResponseEntity<Object> querySettings(){
        log.info("===通用设置读取");
        return userService.querySettings();
    }

    //修改通知设置
    @PostMapping("/notifications/setting")
    public ResponseEntity<Object> updateSettings(@RequestBody Settings settings){

        log.info("===通用设置读取");
        return userService.updateSettings(settings);
    }

    ///users/questions
    //设置陌生人问题
    @PostMapping("/questions")
    public ResponseEntity<Object> saveQuestions(@RequestBody Map<String,String> map){
        String content = map.get("content");
        log.info("===content:",content);
        return userService.saveQuestions(content);
    }

    ///users/blacklist
    @GetMapping("blacklist")
    public ResponseEntity<Object> findBlacklist(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10")Integer pagesize){
        return userService.blacklist(page,pagesize);
    }

    ///users/blacklist/:uid
    @DeleteMapping("/blacklist/{uid}")
    public ResponseEntity<Object> deleteBlack(
            @PathVariable("uid") Long uid){
        return userService.deleteBlack(uid);
    }
}
