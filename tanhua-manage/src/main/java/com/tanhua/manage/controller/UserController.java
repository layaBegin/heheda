package com.tanhua.manage.controller;

import com.tanhua.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/manage")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Object> findByPage(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer pagesize) throws InvocationTargetException, IllegalAccessException {

        return userService.findByPage(page,pagesize);
    }
    ///manage/users/:userID
    @GetMapping("/users/{userID}")
    public ResponseEntity<Object> getUserDetail(@PathVariable("userID") String userID){
        return userService.getUserDetail(userID);
    }

    ///manage/videos
    @GetMapping("/videos")
    public ResponseEntity<Object> getVideoPage(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer pagesize,Long uid){
        return userService.getVideoPage(page,pagesize,uid);
    }
    ///manage/messages
    @GetMapping("/messages")
    public ResponseEntity<Object> getUserMessagesPage(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer pagesize,Long uid,String state){

        return userService.getUserMessagesPage(page,pagesize,uid,state);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<Object> getUserMessageDetail(@PathVariable("id") String  publishId){

        return userService.getUserMessageDetail(publishId);
    }

    @GetMapping("/messages/comments")
    public ResponseEntity<Object> getComment(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer pagesize,String sortProp,String sortOrder,String messageID){
        return userService.getComment(page,pagesize,sortProp,sortOrder,messageID);
    }
}
