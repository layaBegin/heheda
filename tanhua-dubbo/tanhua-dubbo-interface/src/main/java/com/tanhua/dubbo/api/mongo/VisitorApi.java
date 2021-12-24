package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.db.Visitor;

import java.util.List;

public interface VisitorApi {

    List<Visitor> getVisitors(Long userId, int i);
    List<Visitor> getVisitors(Long userId, long i);

    void save(Visitor visitor);
}
