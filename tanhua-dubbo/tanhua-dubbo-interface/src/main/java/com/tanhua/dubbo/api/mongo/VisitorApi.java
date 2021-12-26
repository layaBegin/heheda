package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.db.Visitor;
import com.tanhua.domain.vo.PageResult;

import java.util.List;

public interface VisitorApi {

    List<Visitor> getVisitors(Long userId, int i);
    List<Visitor> getVisitors(Long userId, long i);

    void save(Visitor visitor);

    PageResult getVisitorPage(Integer page, Integer pagesize, Long userId);
}
