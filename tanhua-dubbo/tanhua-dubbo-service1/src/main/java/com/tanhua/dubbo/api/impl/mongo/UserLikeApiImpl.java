package com.tanhua.dubbo.api.impl.mongo;

import com.tanhua.domain.mongo.db.UserLike;
import com.tanhua.dubbo.api.mongo.UserLikeApi;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Service
public class UserLikeApiImpl implements UserLikeApi {

    @Autowired
    private  MongoTemplate mongoTemplate;

    @Override
    public Integer loveCount(Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        Long count = mongoTemplate.count(query, UserLike.class);
        return count.intValue();
    }

    @Override
    public Integer fanCount(Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("likeUserId").is(userId));
        Long count = mongoTemplate.count(query, UserLike.class);
        return count.intValue();
    }

    @Override
    public void save(UserLike userLike) {
        mongoTemplate.save(userLike);
    }

    @Override
    public Boolean alreadyLove(Long userId, Long uid) {
        Query query = new Query();
        query.addCriteria(Criteria.where("likeUserId").is(uid).and("userId").is(userId));
        long count = mongoTemplate.count(query, UserLike.class);
        return count > 0 ;
    }

    @Override
    public void delete(Long userId, Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("likeUserId").is(id).and("userId").is(userId));
        mongoTemplate.remove(query, UserLike.class);
    }
}
