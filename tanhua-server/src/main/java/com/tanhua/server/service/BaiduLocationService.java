package com.tanhua.server.service;

import com.tanhua.domain.mongo.db.Location;
import com.tanhua.dubbo.api.mongo.LocationApi;
import com.tanhua.server.utils.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BaiduLocationService {

    @Reference
    private LocationApi locationApi;


    public ResponseEntity<Object> saveLocation(Double latitude, Double longitude, String addrStr) {
        Long userId = UserHolder.getUserId();

        Location location = new Location();
        location.setAddress(addrStr);
        location.setLocation(new GeoJsonPoint(longitude,latitude));
        location.setUserId(userId);
        locationApi.saveLocation(latitude,longitude,addrStr,userId);
        return ResponseEntity.ok(null);
    }
}
