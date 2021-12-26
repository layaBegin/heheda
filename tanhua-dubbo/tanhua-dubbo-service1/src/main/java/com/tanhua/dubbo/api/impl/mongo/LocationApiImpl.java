package com.tanhua.dubbo.api.impl.mongo;

import com.tanhua.domain.mongo.db.Location;
import com.tanhua.domain.mongo.vo.UserLocationVo;
import com.tanhua.dubbo.api.mongo.LocationApi;
import org.apache.dubbo.config.annotation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationApiImpl implements LocationApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveLocation(Double latitude, Double longitude, String addrStr, Long userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        boolean exists = mongoTemplate.exists(query, Location.class);
        if (exists) {
            Update update = new Update();
            update.set("location",new GeoJsonPoint(longitude,latitude));
            update.set("updated",System.currentTimeMillis());
            update.set("lastUpdated",System.currentTimeMillis());
            update.set("address",addrStr);
            mongoTemplate.updateFirst(query,update,Location.class);
        }else {
            Long time = System.currentTimeMillis();
            Location location = new Location();
            location.setCreated(time);
            location.setUpdated(time);
            location.setLastUpdated(time);
            location.setLocation(new GeoJsonPoint(longitude,latitude));
            location.setUserId(userId);
            location.setAddress(addrStr);
            mongoTemplate.save(location);
        }
    }

    @Override
    public List<Long> searchNear(Long userId, Long distance) {
        //1. 查询当前用户的地理位置
        Query query = new Query(Criteria.where("userId").is(userId));
        Location userLocation = mongoTemplate.findOne(query, Location.class);
        // 获取用户的坐标
        GeoJsonPoint location = userLocation.getLocation();

        //2. 准备参数
        //2.1 画圆半径的距离
        Distance distanceObj = new Distance(distance/1000, Metrics.KILOMETERS);
        //2.2 根据用户的坐标画圆
        Circle circle = new Circle(location,distanceObj);

        //3. 根据当前用户的坐标画圆，搜索附近的人
        Query locationQuery = new Query(
                Criteria.where("location").withinSphere(circle)
        );
        List<Location> locationList = mongoTemplate.find(locationQuery, Location.class);
        List<Long> nearUserList = new ArrayList<>();
        for (Location location1:
                locationList) {
            Long userId1 = location1.getUserId();

            nearUserList.add(userId1);

        }
        return nearUserList;
    }
}
