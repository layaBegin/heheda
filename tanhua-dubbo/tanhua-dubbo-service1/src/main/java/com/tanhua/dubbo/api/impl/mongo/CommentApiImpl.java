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
        //1,commont表 添加一条评论
        mongoTemplate.save(comment);
        String col = comment.getCol();
        //2,Publish 中添加点赞数；
        Query queryUpdate = new Query(Criteria.where("id").is(comment.getPublishId()));
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
        List<Comment> comments =  mongoTemplate.find(query,Comment.class);


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
}
