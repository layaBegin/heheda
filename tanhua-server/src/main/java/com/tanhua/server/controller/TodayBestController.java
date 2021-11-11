package com.tanhua.server.controller;

import com.tanhua.domain.db.User;
import com.tanhua.domain.mongo.vo.RecommendationVo;
import com.tanhua.domain.vo.ErrorResult;
import com.tanhua.server.service.TodayBestService;
import com.tanhua.server.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

///tanhua/todayBest
@RestController
@RequestMapping("/tanhua")
@Slf4j
public class TodayBestController {


    @Autowired
    TodayBestService todayBestService;

    @GetMapping("/todayBest")
    public ResponseEntity<Object> getTodayBest(){
        return todayBestService.getTodayBest();
    }

    ///tanhua/recommendation
    /**
     * 接口名称：推荐朋友 (首页推荐)
     * 接口路径：GET/tanhua/recommendation
     */
    @GetMapping("recommendation")
    public ResponseEntity<Object> recommendation(RecommendationVo vo){
        return todayBestService.getRecommendation(vo);
    }

}
