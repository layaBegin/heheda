package com.tanhua.server.service;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.db.Video;
import com.tanhua.domain.mongo.vo.VideoVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.mongo.PublishApi;
import com.tanhua.dubbo.api.mongo.VideoApi;
import com.tanhua.server.utils.UserHolder;
import org.apache.commons.io.FileUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService {

    @Reference
    private VideoApi videoApi;

    @Reference
    private UserInfoApi userInfoApi;
    @Autowired
    private OssTemplate ossTemplate;
    @Autowired
    private FastFileStorageClient fSClient;
    @Autowired
    private FdfsWebServer webServer;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public ResponseEntity<Object> getVideoList(Integer page, Integer pagesize) {
        Long userId = UserHolder.getUserId();
        PageResult pageResult =  videoApi.findVideoPage(page,pagesize);
        List<Video> videoList = (List<Video>)pageResult.getItems();
        List<VideoVo> videoVos = new ArrayList<>();
        for (Video video:
                videoList) {
            VideoVo videoVo = new VideoVo();
            BeanUtils.copyProperties(video,videoVo);
            videoVo.setId(video.getId().toString());
            videoVo.setUserId(video.getUserId().intValue());
            videoVo.setCover(video.getPicUrl());
            videoVo.setSignature(video.getText());
            UserInfo userInfo = userInfoApi.findById(video.getUserId());
            videoVo.setAvatar(userInfo.getAvatar());
            videoVo.setNickname(userInfo.getNickname());
            //hasFocus 是否关注，应该是查redis

            //hasLiked 是否点赞，查redis
            videoVos.add(videoVo);
        }
        pageResult.setItems(videoVos);
        return ResponseEntity.ok(pageResult);
    }



    public ResponseEntity<Object> uploadVideo(MultipartFile videoThumbnail, MultipartFile videoFile, String text) throws IOException {
        Long userId = UserHolder.getUserId();
        Video video = new Video();
        String url = ossTemplate.upload(videoThumbnail.getOriginalFilename(), videoThumbnail.getInputStream());
        //封面放在阿里云
        video.setPicUrl(url);
        //视频放在fastDfs
        //获取上传后缀名
        String originalFilename = videoFile.getOriginalFilename();
        System.out.println("===originalFilename:"+originalFilename);
        String sufix = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        StorePath storePath = fSClient.uploadFile(videoFile.getInputStream(), videoFile.getSize(), sufix, null);
        String fullPath = storePath.getFullPath();
        System.out.println("==fullPath:"+ fullPath);
        String s = webServer.getWebServerUrl() + fullPath;

        video.setVideoUrl(s);
        video.setText(text);
        video.setCreated(System.currentTimeMillis());
        video.setUserId(userId);
        System.out.println("==s:"+s);
        videoApi.save(video);
        return ResponseEntity.ok(null);
    }

    public ResponseEntity<Object> userFocus(String uid) {

        return null;
    }
}
