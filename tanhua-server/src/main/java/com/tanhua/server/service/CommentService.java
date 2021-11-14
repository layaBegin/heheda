package com.tanhua.server.service;


import com.tanhua.domain.mongo.db.Comment;
import com.tanhua.dubbo.api.mongo.CommentApi;
import com.tanhua.server.utils.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Reference
    private CommentApi commentApi;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public ResponseEntity<Object> saveLike(String publishId) {
        Long userId = UserHolder.getUserId();
        ObjectId objectId = new ObjectId(publishId);
        Comment comment = new Comment();
        comment.setPublishId(objectId);
        comment.setUserId(userId);
        comment.setCommentType(1);
        comment.setCreated(System.currentTimeMillis());
        Long count = commentApi.save(comment);

        String key = "public_like_comment_" + userId + publishId;
        redisTemplate.opsForValue().set(key,"1");

        return ResponseEntity.ok(count.intValue());
    }
}
