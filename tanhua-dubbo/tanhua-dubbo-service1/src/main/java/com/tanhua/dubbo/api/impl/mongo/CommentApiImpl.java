package com.tanhua.dubbo.api.impl.mongo;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.db.Comment;
import com.tanhua.domain.mongo.db.Publish;
import com.tanhua.domain.mongo.vo.CommentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.mongo.CommentApi;
import org.apache.dubbo.config.annotation.Service;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;


@Service
public class CommentApiImpl implements CommentApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Long save(Comment comment) {

        Query queryUpdate = new Query(Criteria.where("id").is(comment.getPublishId()));
        Publish publish = mongoTemplate.findById(comment.getPublishId(), Publish.class);
        Long userId = publish.getUserId();
        //把comment表增加一个字段，谁评论谁，一目了然
        //1,commont表 添加一条评论
        comment.setPuserId(userId);
        mongoTemplate.save(comment);

        String col = comment.getCol();
        Update update = new Update();
        update.inc(col,1); //inc 增加或减少数字，原子增加，保证线程安全
        //2，更新publish 表中的点赞数
        UpdateResult updateResult = mongoTemplate.updateFirst(queryUpdate, update, Publish.class);//修改找到的第一个
        //
        Query query1 = new Query(Criteria.where("publishId").is(comment.getPublishId()).
                and("commentType").is(comment.getCommentType()));
        long count = mongoTemplate.count(query1, Comment.class);

        return count;
    }

    @Override
    public Long delete(Comment comment) {
        String col = comment.getCol();
        Update update = new Update();
        update.inc(col,-1); //inc 增加或减少数字，原子增加，保证线程安全UpdateResult update Result = mongoTemplate.updateFirst(query, update, Publish.class);//修改找到的第一个
        //1，更新publish 表中的点赞数
        Query queryPublish = new Query(Criteria.where("id").is(comment.getPublishId()));
        UpdateResult updateResult = mongoTemplate.updateFirst(queryPublish, update, Publish.class);//修改找到的第一个

        //2,删除评论表数据
        Query query = new Query(Criteria.where("publishId").is(comment.getPublishId()).and("userId").is(comment.getUserId()).
                and("commentType").is(comment.getCommentType()));
        DeleteResult remove = mongoTemplate.remove(query, Comment.class);
        Query queryCount = new Query(Criteria.where("publishId").is(comment.getPublishId()).
                and("commentType").is(comment.getCommentType()));
        long count = mongoTemplate.count(queryCount, Comment.class);
        return count;
    }

    //查找评论数
    @Override
    public PageResult findComments(String movementId, Integer page, Integer pagesize) {
        Query query = new Query(Criteria.where("publishId").is(new ObjectId(movementId)).and("commentType").is(2));
        query.with(Sort.by(Sort.Order.desc("created")));
        query.limit(pagesize).skip((page-1)* pagesize);
        List<Comment> comments = mongoTemplate.find(query,Comment.class);


        PageResult pageResult = new PageResult(page,pagesize,comments.size(),comments);
        return pageResult;
    }

    @Override
    public void updateLikeCount(String id, Integer num) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        Update update = new Update();
        update.inc("likeCount",num);
        UpdateResult updateResult = mongoTemplate.updateFirst(query,update, Comment.class);

    }

    @Override
    public PageResult findListByPuserId(Integer page, Integer pagesize, Long userId,Integer commentType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("puserId").is(userId).and("commentType").is(commentType));
        query.with(Sort.by(Sort.Order.desc("created")));
        query.skip((page - 1) * pagesize).limit(pagesize);
        List<Comment> comments = mongoTemplate.find(query, Comment.class);
        Long counts = mongoTemplate.count(query, Comment.class);
        PageResult pageResult = new PageResult(page,pagesize,counts.intValue(),comments);

        return pageResult;
    }
}
