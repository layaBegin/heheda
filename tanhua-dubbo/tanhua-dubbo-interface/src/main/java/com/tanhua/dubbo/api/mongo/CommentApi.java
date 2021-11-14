package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.db.Comment;


public interface CommentApi {

    Long save(Comment comment);
}
