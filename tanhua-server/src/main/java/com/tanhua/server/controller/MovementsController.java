package com.tanhua.server.controller;

import com.tanhua.domain.mongo.db.Publish;
import com.tanhua.server.service.MovementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/movements")
public class MovementsController {

    @Autowired
    private MovementsService movementsService;

    /**
     * 接口名称：动态-发布
     * 接口路径：POST/movements
     */
    @PostMapping
    public ResponseEntity<Object> saveMovements(
            Publish publish, MultipartFile[] imageContent) throws Exception {
        return movementsService.saveMovements(publish,imageContent);
    }

    /**
     * 接口名称：好友动态
     * 接口路径：GET/movements
     * 需求描述：查询好友动态，就是查看“朋友圈”。在自己的时间线表中存储了所有好友的动态
     * 举例：登陆用户id是20，查询好友动态就是查询 db.quanzi_time_line_20.find()
     */
    @GetMapping
    public ResponseEntity<Object> queryPublishList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pagesize) throws IOException {
        if(page<1) page = 1;
        return movementsService.queryPublishList(page,pagesize);
    }

    ///movements/recommend
    @GetMapping("/recommend")
    public ResponseEntity<Object> queryRecommend(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pagesize) throws IOException {
        if(page<1) page = 1;
        return movementsService.queryRecommend(page,pagesize);
    }

    ///movements/all
    @GetMapping("/all")
    public ResponseEntity<Object> queryUserMovements(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pagesize,Long userId) throws IOException {
        if(page<1) page = 1;

        return movementsService.queryUserMovements(page,pagesize,userId);
    }
}
