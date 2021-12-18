package com.tanhua.dubbo.api.impl.mongo;

import com.tanhua.domain.mongo.db.Video;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.mongo.VideoApi;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;


@Service
public class VideoApiImpl implements VideoApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Video video) {
        mongoTemplate.save(video);
    }

    @Override
    public PageResult findVideoPage(Integer page, Integer pagesize) {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("created")));
        query.skip((page - 1) * pagesize).limit(pagesize);
        List<Video> videoList =  mongoTemplate.find(query, Video.class);
        Long count = mongoTemplate.count(query, Video.class);
        System.out.println("===视频总条数count:"+count);
        PageResult pageResult = new PageResult(page, pagesize, count.intValue(), videoList);
        return pageResult;
    }
}
