package com.tanhua.server.controller;


import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/baidu")
public class BaiduLocationController {

    @Reference
    private LocationApi locationApi;
    ///baidu/location

    @PostMapping("/location")
    public ResponseEntity<Object> saveLocation(@RequestBody Map<String,Object> paramMap){


    }
}
