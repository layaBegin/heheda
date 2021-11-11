package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.db.RecommendUser;
import com.tanhua.domain.vo.PageResult;

import java.util.List;

public interface RecommendUserApi {
    /*
    根据用户id查询缘分值最高的用户
     */
    RecommendUser queryWithMaxScore(Long id);

    PageResult findRecommendation(Long userId,Integer page,Integer pagesize);
}
