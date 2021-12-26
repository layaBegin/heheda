package com.tanhua.server.controller;


import com.tanhua.dubbo.api.mongo.LocationApi;
import com.tanhua.server.service.BaiduLocationService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/baidu")
public class BaiduLocationController {



    @Autowired
    private BaiduLocationService baiduLocationService;
    ///baidu/location

    @PostMapping("/location")
    public ResponseEntity<Object> saveLocation(@RequestBody Map<String,Object> paramMap){
        Double latitude = (Double)paramMap.get("latitude");
        Double longitude = (Double)paramMap.get("longitude");
        String addrStr = (String)paramMap.get("addrStr");

        return baiduLocationService.saveLocation(latitude,longitude,addrStr);
    }
}
