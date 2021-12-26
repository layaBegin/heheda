package com.tanhua.dubbo.api.impl.mongo;

import com.tanhua.domain.mongo.db.RecommendUser;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.mongo.RecommendUserApi;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Service
public class RecommendUserApiImpl implements RecommendUserApi {

    @Autowired
    private MongoTemplate mongoTemplate;
    //db.recommend_user.find({userId:124}).sort({score:-1}).limit(1)
    @Override
    public RecommendUser queryWithMaxScore(Long userId) {
        Query query = new Query(
                Criteria.where("userId").is(userId)
        );
        query.with(Sort.by(Sort.Order.desc("score")));
        query.limit(1);
        RecommendUser recommendUser = mongoTemplate.findOne(query, RecommendUser.class);
        return recommendUser;
    }

    @Override
    public PageResult findRecommendation(Long userId,Integer page,Integer pagesize) {
        Query query = new Query(
                Criteria.where("userId").is(userId)
        );
        query.with(Sort.by(Sort.Order.desc("score")));
        query.skip((page-1)*pagesize);
        query.limit(pagesize);

        List<RecommendUser>  recommendUsers =  mongoTemplate.find(query,RecommendUser.class);
        long count = mongoTemplate.count(query, RecommendUser.class);
        return new PageResult(page,pagesize,(int)count,recommendUsers);

    }

    @Override
    public RecommendUser findByRecommendId(Long recommendId,Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("recommendUserId").is(recommendId).and("userId").is(userId));
        RecommendUser recommendUser = mongoTemplate.findOne(query, RecommendUser.class);
        return recommendUser;
    }

    @Override
    public Integer queryScore(Long userId, Long likeUserId) {
        Query query = new Query(Criteria.where("userId").is(userId).and("recommendUserId").is(likeUserId));
        RecommendUser one = mongoTemplate.findOne(query, RecommendUser.class);
        Integer fature = 0;
        if (one != null){
            Double score = one.getScore();
            fature = score.intValue();
        }else {
            fature = 60;
        }
        return fature;
    }
}
