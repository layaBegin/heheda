package com.tanhua.dubbo.test;

import com.tanhua.domain.mongo.db.Video;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoTest {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Test
    public void testSaveVideos() {

        for (int i = 1; i <=2; i++) {
            Video video = new Video();
            video.setPicUrl("https://tanhua-dev.oss-cn-zhangjiakou.aliyuncs.com/images/video/video_"+i+".png");
            video.setVideoUrl("https://tanhua-dev.oss-cn-zhangjiakou.aliyuncs.com/images/video/1576134125940400.mp4");
            video.setId(ObjectId.get());
            video.setUserId((long)i);
            video.setCreated(System.currentTimeMillis());
            video.setText("我就是我不一样的烟火~");
            mongoTemplate.save(video);
        }
    }
}