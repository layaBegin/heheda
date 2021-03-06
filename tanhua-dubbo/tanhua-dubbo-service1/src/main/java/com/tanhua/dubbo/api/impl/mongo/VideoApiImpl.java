package com.tanhua.dubbo.api.impl.mongo;

import com.tanhua.domain.mongo.db.FollowUser;
import com.tanhua.domain.mongo.db.Video;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.mongo.VideoApi;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
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
    public PageResult findVideoPage(Integer page, Integer pagesize,Long...userId) {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("created")));
        query.skip((page - 1) * pagesize).limit(pagesize);
        if (userId != null){
            query.addCriteria(Criteria.where("userId").is(userId[0]));
        }
        List<Video> videoList = mongoTemplate.find(query, Video.class);
        Long count = mongoTemplate.count(query, Video.class);
        System.out.println("===视频总条数count:"+count);
        PageResult pageResult = new PageResult(page, pagesize, count.intValue(), videoList);
        return pageResult;
    }

    @Override
    public void userFocus(FollowUser followUser) {
        mongoTemplate.save(followUser);

    }

    @Override
    public void userUnFocus(FollowUser followUser) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(followUser.getUserId()).and("followUserId").is(followUser.getFollowUserId()));
        mongoTemplate.remove(query,FollowUser.class);

    }




}
