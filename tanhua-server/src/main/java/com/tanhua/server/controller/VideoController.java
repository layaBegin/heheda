package com.tanhua.server.controller;

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

        return videoService.getVideoList(page,pagesize);
    }

    //发布视频
    @PostMapping
    public ResponseEntity<Object> uploadVideo(MultipartFile videoThumbnail,MultipartFile videoFile,String text) throws IOException {
        return videoService.uploadVideo(videoThumbnail,videoFile,text);
    }

    ///smallVideos/:uid/userFocus
    //发布视频
    @PostMapping("/{uid}/userFocus")
    public ResponseEntity<Object> userFocus(@PathVariable("uid") String uid) throws IOException {
        return videoService.userFocus(uid);
    }
}
