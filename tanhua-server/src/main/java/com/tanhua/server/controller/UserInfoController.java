package com.tanhua.server.controller;

import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jws.Oneway;
import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserInfoController {


    @Autowired
    private UserService userService;

    /**
     * 接口名称：用户资料 - 读取
     * 接口路径：GET/users
     * 需求描述：
     *   1.根据用户id查询用户详情
     *   2.如果userID不为NULL，根据用户id查询
     *   3.如果用户id为空，huanxinID不为NULL，根据环信ID查询
     *   4.如果userID与huanxinID都为空，从token获取用户id
     */
    @GetMapping
    public ResponseEntity<Object> findUserInfo(Long userID,Long huanxinID){

        return userService.findUserInfoById(userID,huanxinID);
    }

    //@RequestBody 将客户端传过来的字段转换为UserInfo对象
    @PutMapping
    public ResponseEntity<Object> updateUserInfo(@RequestBody UserInfo userInfo){

        return userService.updateUserInfo(userInfo);
    }


    ///users/counts

    @GetMapping("/counts")
    public ResponseEntity<Object> queryFoucsCounts(){
        return userService.queryFoucsCounts();
    }

    ///users/:uid/alreadyLove
    @GetMapping("/{uid}/alreadyLove")
    public ResponseEntity<Object> queryFoucsCounts(@PathVariable("uid") Long uid){
        return userService.alreadyLove(uid);
    }

    ///users/friends/:type
    // 1 互相关注
    //2 我关注
    //3 粉丝
    //4 谁看过我
    @GetMapping("/friends/{type}")
    public ResponseEntity<Object> friendsTypeList(
            @PathVariable("type") Integer type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "1") Integer pagesize,
            String nickname){
        return userService.friendsTypeList(type,page,pagesize);
    }

    ///users/fans/:uid
    @PostMapping("/fans/{uid}")
    public ResponseEntity<Object> loveFans(
            @PathVariable("uid") Long uid){
        return userService.loveFans(uid);
    }

    ///users/like/:uid
    @DeleteMapping("/like/{uid}")
    public ResponseEntity<Object> unLike(@PathVariable("uid") Long uid){

        return  userService.unLike(uid);
    }

}