package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.db.Comment;
import com.tanhua.domain.vo.PageResult;
import org.springframework.data.domain.PageRequest;


public interface CommentApi {

    //保存评论
    Long save(Comment comment);

    //获取评论
    Long delete(Comment comment);

    PageResult findComments(String movementId, Integer page, Integer pagesize);

    void updateLikeCount(String id,Integer num);

    PageResult findListByPuserId(Integer page, Integer pagesize, Long userId,Integer commentType);
}
