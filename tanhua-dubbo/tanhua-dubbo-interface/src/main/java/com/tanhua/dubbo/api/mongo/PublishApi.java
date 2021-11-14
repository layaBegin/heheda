package com.tanhua.dubbo.api.mongo;


import com.tanhua.domain.mongo.db.Publish;
import com.tanhua.domain.vo.PageResult;
import org.bson.types.ObjectId;

public interface PublishApi {
    /**
     * 发布动态
     */
    void save(Publish publish);
    /**
     * 分页查询好友动态（自己的时间线表）
     */
    PageResult findByTimeLine(Integer page, Integer pagesize, Long userId);


    PageResult findRecommend(Long userId, Integer page, Integer pagesize);

    PageResult findAlbum(Integer page, Integer pagesize, Long userId);

}