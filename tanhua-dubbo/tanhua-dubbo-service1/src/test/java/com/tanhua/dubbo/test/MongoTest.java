package com.tanhua.dubbo.test;

import com.tanhua.domain.mongo.db.RecommendUser;
import com.tanhua.dubbo.api.mongo.RecommendUserApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoTest {
    @Autowired
    private RecommendUserApi recommendUserApi;

    @Test
    public void test1(){
        RecommendUser recommendUsers = recommendUserApi.queryWithMaxScore(124L);
        System.out.println("==recommendUsers:"+recommendUsers);

    }
}
