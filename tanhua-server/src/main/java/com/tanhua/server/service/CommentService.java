package com.tanhua.server.service;


import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.db.Comment;
import com.tanhua.domain.mongo.vo.CommentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.mongo.CommentApi;
import com.tanhua.dubbo.api.mongo.PublishApi;
import com.tanhua.server.utils.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Reference
    private CommentApi commentApi;
    @Reference
    private UserInfoApi userInfoApi;
    @Reference
    private PublishApi publishApi;

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

    public ResponseEntity<Object> disLike(String publishId) {
        Long userId = UserHolder.getUserId();
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setCommentType(1);
        comment.setPublishId(new ObjectId(publishId));
        Long count = commentApi.delete(comment);
        String key = "public_like_comment_" + userId + publishId;
        redisTemplate.delete(key);
        return ResponseEntity.ok(count.intValue());
    }

    //3-喜欢
    public ResponseEntity<Object> love(String publishId) {
        Long userId = UserHolder.getUserId();
        ObjectId objectId = new ObjectId(publishId);
        Comment comment = new Comment();
        comment.setPublishId(objectId);
        comment.setUserId(userId);
        comment.setCommentType(3);
        comment.setCreated(System.currentTimeMillis());
        Long count = commentApi.save(comment);

        String key = "public_love_comment_" + userId + publishId;
        redisTemplate.opsForValue().set(key,"1");

        return ResponseEntity.ok(count.intValue());

    }


    public ResponseEntity<Object> unlove(String publishId) {

        Long userId = UserHolder.getUserId();
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setCommentType(3);
        comment.setPublishId(new ObjectId(publishId));
        Long count = commentApi.delete(comment);
        String key = "public_love_comment_" + userId + publishId;
        redisTemplate.delete(key);
        return ResponseEntity.ok(count.intValue());
    }

    // 获取评论列表
    public PageResult getCommentPage(String movementId, Integer page, Integer pagesize) {
        Long userId = UserHolder.getUserId();
        PageResult pageResult = commentApi.findComments(movementId,page,pagesize);
        List<Comment> comments = (List<Comment>) pageResult.getItems();
        List<CommentVo> commentsVo = new ArrayList<>();
        for (Comment comment :
                comments) {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment,commentVo);
            UserInfo userInfo = userInfoApi.findById(comment.getUserId());
            BeanUtils.copyProperties(userInfo,commentVo);
            commentVo.setId(comment.getId().toString());
            commentVo.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(comment.getCreated())));

            String key = "public_hasLiked_comment_" + userId + comment.getId().toString();
            if (redisTemplate.hasKey(key) ){
                commentVo.setHasLiked(1);
            }else {
                commentVo.setHasLiked(0);
            }
            commentsVo.add(commentVo);
        }
        pageResult.setItems(commentsVo);
        return pageResult;
    }

    public ResponseEntity<Object> commitComment(String movementId, String commentContent) {
        Long userId = UserHolder.getUserId();
        Comment comment = new Comment();
        comment.setPublishId(new ObjectId(movementId));
        comment.setUserId(userId);
        comment.setCommentType(2);
        comment.setContent(commentContent);
        comment.setCreated(System.currentTimeMillis());
        Long save = commentApi.save(comment);
        return ResponseEntity.ok(null);
    }

    public ResponseEntity<Object> likeComment(String id) {
        Long userId = UserHolder.getUserId();
        String key = "public_hasLiked_comment_" + userId + id;
        redisTemplate.opsForValue().set(key,"1");
        commentApi.updateLikeCount(id,1);

        return ResponseEntity.ok(null);
    }

    public ResponseEntity<Object> dislikeComment(String id) {
        Long userId = UserHolder.getUserId();
        String key = "public_hasLiked_comment_" + userId + id;
        redisTemplate.delete(key);
        commentApi.updateLikeCount(id,-1);
        return ResponseEntity.ok(null);
    }
}
