package com.tanhua.server.test;

import com.tanhua.dubbo.api.mongo.LocationApi;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationTest {

    @Reference
    private LocationApi locationApi;

    @Test
    public void addLocation(){
        locationApi.saveLocation(39.914938,116.403695,"广州",125L);
        locationApi.saveLocation(39.914937,116.403693,"广州",79L);
    }
}
