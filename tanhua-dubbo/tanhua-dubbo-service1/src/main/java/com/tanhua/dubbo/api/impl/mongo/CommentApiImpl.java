package com.tanhua.dubbo.api.impl.mongo;

import com.mongodb.client.result.UpdateResult;
import com.tanhua.domain.mongo.db.Comment;
import com.tanhua.domain.mongo.db.Publish;
import com.tanhua.dubbo.api.mongo.CommentApi;
import org.apache.dubbo.config.annotation.Service;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


@Service
public class CommentApiImpl implements CommentApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Long save(Comment comment) {
        //1,添加一条评论
        mongoTemplate.save(comment);
        String col = comment.getCol();
        //2,Publish 中添加点赞数；
        Query query = new Query(Criteria.where("publishId").is(comment.getPublishId()));
        Update update = new Update();
        update.inc(col,1); //inc 增加或减少数字
        mongoTemplate.updateFirst(query, update, Publish.class);

        Query query1 = new Query(Criteria.where("publishId").is(comment.getPublishId()).
                and("commentType").is(comment.getCommentType()));
        long count = mongoTemplate.count(query1, Comment.class);

        return count;
    }
}
