package com.tanhua.server.service;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.tanhua.domain.db.User;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.db.RecommendUser;
import com.tanhua.domain.mongo.vo.RecommendationVo;
import com.tanhua.domain.mongo.vo.TodayBestVo;
import com.tanhua.domain.vo.ErrorResult;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.mongo.RecommendUserApi;
import com.tanhua.server.utils.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TodayBestService {

    @Reference
    private RecommendUserApi recommendUserApi;
    @Reference
    private UserInfoApi userInfoApi;

    public ResponseEntity<Object> getTodayBest() {
        //1,校验token
        User user = UserHolder.get();
        if (user == null){
            return ResponseEntity.status(500).body(ErrorResult.error());
        }
        Long id = user.getId();
        RecommendUser recommendUser = recommendUserApi.queryWithMaxScore(id);
        if (recommendUser == null){
            recommendUser = new RecommendUser();
            recommendUser.setRecommendUserId(10L);
            recommendUser.setScore(60D);
        }
        TodayBestVo todayBestVo = _userInfoCastToTodayBestVo(recommendUser);
        return ResponseEntity.ok(todayBestVo);
    }

    private TodayBestVo _userInfoCastToTodayBestVo(RecommendUser recommendUser){
        Long recommendUserId = recommendUser.getRecommendUserId();
        UserInfo userInfo = userInfoApi.findById(recommendUserId);
        TodayBestVo todayBestVo = new TodayBestVo();
        if (userInfo != null){
            BeanUtils.copyProperties(userInfo,todayBestVo);
            if (userInfo.getTags() != null){
                todayBestVo.setTags(userInfo.getTags().split(","));
            }
            todayBestVo.setFateValue(recommendUser.getScore().longValue());
        }
        return todayBestVo;
    }

    public ResponseEntity<Object> getRecommendation(RecommendationVo recommendationVo) {
        //1,校验token
        User user = UserHolder.get();
        if (user == null){
            return ResponseEntity.status(500).body(ErrorResult.error());
        }
        Long userId = user.getId();
        PageResult result =  recommendUserApi.findRecommendation(userId,recommendationVo.getPage(),recommendationVo.getPagesize());

        List<RecommendUser> recommendUsers = (List<RecommendUser>) result.getItems();


        List<TodayBestVo> todayBestVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(recommendUsers)){
            for (RecommendUser recommendUser : recommendUsers)  {
                TodayBestVo todayBestVo = _userInfoCastToTodayBestVo(recommendUser);
                todayBestVos.add(todayBestVo);
            }
        }
        result.setItems(todayBestVos);
        return ResponseEntity.ok(result);
    }
}
