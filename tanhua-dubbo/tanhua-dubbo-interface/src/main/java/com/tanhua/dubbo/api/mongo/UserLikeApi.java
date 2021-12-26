package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.db.UserLike;
import com.tanhua.domain.vo.PageResult;

public interface UserLikeApi {
    Integer loveCount(Long userId);

    Integer fanCount(Long userId);

    void save(UserLike userLike);

    Boolean alreadyLove(Long userId, Long uid);

    void delete(Long userId, Long id);

    PageResult getLikePage(Integer page, Integer pagesize, Long userId);

    PageResult getFansPage(Integer page, Integer pagesize, Long userId);
}
