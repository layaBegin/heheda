package com.tanhua.server.controller;

import com.tanhua.domain.vo.PageResult;
import com.tanhua.server.service.IMService;
import com.tanhua.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("messages")
public class IMController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private IMService imService;

    ///messages/contacts
    //添加好友
    @PostMapping("/contacts")
    public ResponseEntity<Object> addContacts(@RequestBody Map<String,Integer> paramMap){
        Integer userId = paramMap.get("userId");
        return imService.addContacts(userId);
    }

    ///messages/contacts
    @GetMapping("/contacts")
    public ResponseEntity<Object> getContantList(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer pagesize, String keyword){
            PageResult pageResult =  imService.getContantList(page,pagesize,keyword);
            return ResponseEntity.ok(pageResult);
    }
    /**
     * 接口名称：根据环信id查询用户信息
     * 接口路径：GET/messages/userinfo
     * 请求参数：huanxinId
     */
    @GetMapping("userinfo")
    public ResponseEntity<Object> queryUserInfoByUserId(Long huanxinId) {
        return userService.findUserInfoById(null,huanxinId);
    }
}