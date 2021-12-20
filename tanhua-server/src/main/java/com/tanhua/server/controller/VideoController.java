package com.tanhua.server.controller;

import com.tanhua.domain.vo.PageResult;
import com.tanhua.server.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/smallVideos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    //获取视频列表
    @GetMapping()
    public ResponseEntity<Object> getVideoList(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer pagesize){

        PageResult videoList = videoService.getVideoList(page, pagesize);
        return ResponseEntity.ok(videoList);
    }

    //发布视频
    @PostMapping
    public ResponseEntity<Object> uploadVideo(MultipartFile videoThumbnail,MultipartFile videoFile,String text) throws IOException {
        videoService.uploadVideo(videoThumbnail,videoFile,text);
        return ResponseEntity.ok(null);
    }

    ///smallVideos/:uid/userFocus 关注
    @PostMapping("/{uid}/userFocus")
    public ResponseEntity<Object> userFocus(@PathVariable("uid") String uid) throws IOException {
        return videoService.userFocus(uid);
    }

    ///smallVideos/:uid/userUnFocus

    @PostMapping("/{uid}/userUnFocus")
    public ResponseEntity<Object> userUnFocus(@PathVariable("uid") String uid) throws IOException {
        return videoService.userUnFocus(uid);
    }
}
