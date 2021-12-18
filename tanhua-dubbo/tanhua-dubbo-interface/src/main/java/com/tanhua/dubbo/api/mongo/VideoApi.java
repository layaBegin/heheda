package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.db.Video;
import com.tanhua.domain.vo.PageResult;

public interface VideoApi {
     void save(Video video);

    PageResult findVideoPage(Integer page, Integer pagesize);
}
