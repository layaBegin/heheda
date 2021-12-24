package com.tanhua.server.controller;

import com.tanhua.domain.db.User;
import com.tanhua.domain.mongo.vo.RecommendationVo;
import com.tanhua.domain.vo.ErrorResult;
import com.tanhua.server.service.IMService;
import com.tanhua.server.service.MovementsService;
import com.tanhua.server.service.TodayBestService;
import com.tanhua.server.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

///tanhua/todayBest
@RestController
@RequestMapping("/tanhua")
@Slf4j
public class TodayBestController {


    @Autowired
    private TodayBestService todayBestService;
    @Autowired
    private IMService imService;
    @Autowired
    private MovementsService movementsService;
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

    ///tanhua/:id/personalInfo
    @GetMapping("/{id}/personalInfo")
    public ResponseEntity<Object> getPersonalInfo(@PathVariable("id") Long id){
        //调用这个接口的时候，写入vistor表
        movementsService.setVisitors(id);

        return todayBestService.getPersonalInfo(id);
    }

    ///tanhua/strangerQuestions
    @GetMapping("/strangerQuestions")
    public ResponseEntity<Object> getStrangerQuestions( Long userId){

        return todayBestService.getStrangerQuestions(userId);
    }

    ///tanhua/strangerQuestions
    @PostMapping("/strangerQuestions")
    public ResponseEntity<Object> postStrangerQuestions( @RequestBody Map<String,Object> paramMap){

        Integer userId = (Integer) paramMap.get("userId");
        String reply = (String) paramMap.get("reply");
        return imService.replyQuestions(userId,reply);
    }

    ///tanhua/:id/love
    @GetMapping("/{id}/love")
    public ResponseEntity<Object> love(@PathVariable("id") Long id){

        return todayBestService.love(id);
    }
}
