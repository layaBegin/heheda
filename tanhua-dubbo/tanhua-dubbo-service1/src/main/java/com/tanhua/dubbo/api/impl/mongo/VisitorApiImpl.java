package com.tanhua.dubbo.api.impl.mongo;

import com.tanhua.domain.mongo.db.RecommendUser;
import com.tanhua.domain.mongo.db.Visitor;
import com.tanhua.dubbo.api.mongo.VisitorApi;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;


@Service
public class VisitorApiImpl implements VisitorApi {

    @Autowired
    private MongoTemplate mongoTemplate;
    //第一次登录
    @Override
    public List<Visitor> getVisitors(Long userId, int i) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.with(Sort.by(Sort.Order.desc("date")));
        query.limit(i);
        List<Visitor> list = mongoTemplate.find(query, Visitor.class);
        return list;
    }

    //以后登录
    @Override
    public List<Visitor> getVisitors(Long userId, long date) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId).and("date").gt(date));
        query.with(Sort.by(Sort.Order.desc("date")));
        List<Visitor> list = mongoTemplate.find(query, Visitor.class);
        return list;
    }

    @Override
    public void save(Visitor visitor) {
        Long userId = visitor.getUserId();
        Long visitorId = visitor.getVisitorId();
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId).and("recommendUserId").is(visitorId));
        RecommendUser recommendUser = mongoTemplate.findOne(query, RecommendUser.class);
        Integer score = 60;
        if  (recommendUser != null){
            score = recommendUser.getScore().intValue();
        }
        visitor.setFateValue(score);
        mongoTemplate.save(visitor);
    }
}
