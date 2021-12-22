package com.tanhua.dubbo.api.impl.mongo;

import com.tanhua.domain.mongo.db.Friend;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.mongo.FriendApi;
import org.apache.dubbo.config.annotation.Service;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Service
public class FriendApiImpl implements FriendApi {

    @Autowired
    private  MongoTemplate mongoTemplate;

    @Override
    public void save(Long userId, Integer friendId) {
        // 添加好友关系到数据库中。好友关系是相互的，比如1的好友是2，同时2的好友也是1
        Query query = new Query(
                Criteria.where("userId").is(userId)
                        .and("friendId").is(friendId)
        );
        /****** userId的好友是 friendId *********/
        // 如果不存在好友关系则添加
        if (!mongoTemplate.exists(query,Friend.class)) {
            Friend friend = new Friend();
            friend.setId(ObjectId.get());
            friend.setUserId(userId);
            friend.setFriendId((long)friendId);
            friend.setCreated(System.currentTimeMillis());
            // 保存好友
            mongoTemplate.save(friend);
        }
        /****** friendId的好友是 userId *********/
        query = new Query(
                Criteria.where("friendId").is(userId)
                        .and("userId").is(friendId)
        );
        if (!mongoTemplate.exists(query,Friend.class)) {
            Friend friend = new Friend();
            friend.setId(ObjectId.get());
            friend.setUserId((long)friendId);
            friend.setFriendId(userId);
            friend.setCreated(System.currentTimeMillis());
            // 保存好友
            mongoTemplate.save(friend);
        }
    }

    @Override
    public PageResult getContantPage(Integer page, Integer pagesize, Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        long counts = mongoTemplate.count(query, Friend.class);
        query.with(Sort.by(Sort.Order.desc("created")));
        query.skip((page - 1) * pagesize).limit(pagesize);
        List<Friend> friends = mongoTemplate.find(query, Friend.class);
        PageResult pageResult = new PageResult(page,pagesize,Integer.parseInt(String.valueOf(counts)),friends);

        return pageResult;
    }
}
