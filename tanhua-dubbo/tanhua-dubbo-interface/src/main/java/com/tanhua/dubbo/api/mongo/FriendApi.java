package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.vo.PageResult;

public interface FriendApi {
    void save(Long userId, Integer friendId);

    PageResult getContantPage(Integer page, Integer pagesize, Long userId);

    Integer count(Long userId);

    Boolean isFriend(Long userId, Long uid);

    void delete(Long userId, Long uid);
}
