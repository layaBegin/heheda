package com.tanhua.dubbo.api.impl.mongo;


import com.mongodb.client.result.UpdateResult;
import com.tanhua.domain.mongo.db.*;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.mongo.PublishApi;
import org.apache.dubbo.config.annotation.Service;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

@Service(timeout = 100000)
public class PublishApiImpl implements PublishApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Publish publish) {
        //发布动态（1）首先将发布的动态内容写入到发布表中  publish
        publish.setId(ObjectId.get());
        publish.setCreated(System.currentTimeMillis());
        mongoTemplate.save(publish);

        //发布动态（2）再将发布的内容保存到自己的相册表（圈子） album
        //比如：登陆用户是20，相册表就是quanzi_album_20
        Album album = new Album();
        album.setId(ObjectId.get());
        album.setPublishId(publish.getId());
        album.setCreated(publish.getCreated());
        mongoTemplate.save(album,"quanzi_album_" + publish.getUserId());

        //发布动态（3）最后，将发布的动态保存到每个好友的时间线表中 time_line
        //比如用户的好友是：2、3
        //对应的时间线表是：time_lime_2、time_lime_3
        //所以需要往上面2个表写入发布的动态

        //A. 先查询好友
        Query query = new Query(Criteria.where("userId").is(publish.getUserId()));
        List<Friend> friendList = mongoTemplate.find(query, Friend.class);
        if (friendList != null) {
            for (Friend friend : friendList) {
                //B. 获取好友id  (2、3)
                Long friendId = friend.getFriendId();
                //C. 往好友的时间线表写入信息
                TimeLine timeLine = new TimeLine();
                timeLine.setId(ObjectId.get());
                timeLine.setUserId(publish.getUserId());
                timeLine.setPublishId(publish.getId());
                timeLine.setCreated(publish.getCreated());
                mongoTemplate.save(timeLine,"quanzi_time_line_" + friendId);
            }
        }
    }


    @Override
    public PageResult findByTimeLine(Integer page, Integer pagesize, Long userId) {
        // 1、查询时间线表
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("created")));
        query.limit(pagesize).skip((page-1)* pagesize);
        List<TimeLine> timeLineList =
                mongoTemplate.find(query, TimeLine.class, "quanzi_time_line_" + userId);
        long count = mongoTemplate.count(query, TimeLine.class, "quanzi_time_line_" + userId);

        // 2、返回发布动态的集合（因为在service要封装MovementsVo，而movementsVo需要获取发布数据）
        List<Publish> publishList = new ArrayList<>();
        if (timeLineList != null) {
            for (TimeLine timeLine : timeLineList) {
                if (timeLine.getPublishId() != null) {
                    // 根据发布动态id查询
                    Publish publish =
                            mongoTemplate.findById(timeLine.getPublishId(), Publish.class);
                    // 添加到集合
                    if (publish != null) {
                        publishList.add(publish);
                    }
                }
            }
        }
        //3、创建分页对象、封装结果并返回
        return new PageResult(page,pagesize, (int) count,publishList);
    }

    @Override
    public PageResult findRecommend(Long userId, Integer page, Integer pagesize) {
        Query query = new Query(Criteria.where("userId").is(userId));
        query.with(Sort.by(Sort.Order.desc("score")));//score 降序排序
        query.limit(pagesize).skip((page-1)* pagesize);
        List<RecommendQuanzi> recommendQuanzis = mongoTemplate.find(query, RecommendQuanzi.class);
        long count = mongoTemplate.count(query, RecommendQuanzi.class);
        List<Publish> publishList = new ArrayList<>();
        for (RecommendQuanzi recommendQuanzi : recommendQuanzis) {
            ObjectId publishId = recommendQuanzi.getPublishId();
            Publish publish = mongoTemplate.findById(publishId, Publish.class);
            if (publish != null) {
                publishList.add(publish);
            }
        }
        PageResult pageResult = new PageResult(page,pagesize,(int)count,publishList);
        return pageResult;
    }

    @Override
    public PageResult findAlbum(Integer page, Integer pagesize, Long userId) {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("created")));
        List<Album> albums = mongoTemplate.find(query, Album.class, "quanzi_album_" + userId);
        long count = mongoTemplate.count(query, Album.class, "quanzi_album_" + userId);

        List<Publish> publishList = new ArrayList<>();
        for (Album album : albums) {
            ObjectId publishId = album.getPublishId();
            Publish publish = mongoTemplate.findById(publishId, Publish.class);
            if (publish != null) {
                publishList.add(publish);
            }
        }
        PageResult result = new PageResult(page,pagesize,(int)count,publishList);

        return result;
    }

    @Override
    public Publish findOne(String publishId) {
        Publish publish = mongoTemplate.findById(new ObjectId(publishId), Publish.class);

        return publish;
    }


}