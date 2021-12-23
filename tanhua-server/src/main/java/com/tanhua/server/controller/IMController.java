package com.tanhua.server.controller;

import com.tanhua.domain.mongo.vo.AnnocementVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.server.service.IMService;
import com.tanhua.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    ///messages/likes
    @GetMapping("/likes")
    public ResponseEntity<Object> getLikesList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pagesize){

        PageResult pageResult =  imService.getLikesList(page,pagesize,1);
        return ResponseEntity.ok(pageResult);
    }

    ///messages/comments
    @GetMapping("/comments")
    public ResponseEntity<Object> getCommentsList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pagesize){

        PageResult pageResult =  imService.getLikesList(page,pagesize,2);
        return ResponseEntity.ok(pageResult);
    }
    ///messages/loves
    @GetMapping("/loves")
    public ResponseEntity<Object> getLovesList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pagesize){

        PageResult pageResult =  imService.getLikesList(page,pagesize,3);
        return ResponseEntity.ok(pageResult);
    }
    ///messages/announcements
    @GetMapping("/announcements")
    public ResponseEntity<Object> getAnnocementsList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pagesize){
        ArrayList<AnnocementVo> annocementVos = new ArrayList<>();
        annocementVos.add(new AnnocementVo("1","测试数据","测试公告","2021-12-23"));
        PageResult pageResult = new PageResult(page, pagesize, 10, annocementVos);
        return ResponseEntity.ok(pageResult);
    }
}