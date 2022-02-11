package com.tanhua.manage.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.db.Publish;
import com.tanhua.domain.mongo.db.Video;
import com.tanhua.domain.mongo.vo.MovementsVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.mongo.PublishApi;
import com.tanhua.dubbo.api.mongo.VideoApi;
import com.tanhua.manage.mapper.AdminMapper;
import com.tanhua.manage.pojo.Admin;
import com.tanhua.manage.vo.MMovementsVo;
import com.tanhua.manage.vo.UserDetailVo;
import com.tanhua.manage.vo.UserPageVo;
import com.tanhua.manage.vo.VideoItemsVo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends ServiceImpl<AdminMapper, Admin> {

    @Reference
    private UserInfoApi userInfoApi;
    @Reference
    private VideoApi videoApi;
    @Reference
    private PublishApi publishApi;

    public ResponseEntity<Object> findByPage(Integer page, Integer pagesize) throws InvocationTargetException, IllegalAccessException {
        IPage<UserInfo> pages = userInfoApi.findByPage(page,pagesize);
        List<UserInfo> records = pages.getRecords();
        List<UserPageVo> list = new ArrayList<>();
        for (UserInfo record :
                records) {
            UserPageVo userPageVo = new UserPageVo();
            BeanUtils.copyProperties(record,userPageVo);
            userPageVo.setId(record.getId().intValue());
            userPageVo.setState("1");
            list.add(userPageVo);
        }
        PageResult pageResult = new PageResult(page,pagesize,(int)pages.getTotal(),list);

        return ResponseEntity.ok(pageResult);
    }

    public ResponseEntity<Object> getUserDetail(String userID) {
        UserInfo userInfo = userInfoApi.findById(Long.parseLong(userID));
        UserDetailVo userDetailVo = new UserDetailVo();
        BeanUtils.copyProperties(userInfo,userDetailVo);
        userDetailVo.setId(Integer.parseInt(userID));
        userDetailVo.setIncome(Integer.parseInt(userInfo.getIncome()));
        userDetailVo.setState("1");
        Long time = System.currentTimeMillis();
        userDetailVo.setCreated(time.intValue());
        return ResponseEntity.ok(userDetailVo);
    }

    public ResponseEntity<Object> getVideoPage(Integer page, Integer pagesize, Long uid) {
        PageResult pageResult =  videoApi.findVideoPage(page,pagesize,uid);
        List<Video> items = (List<Video>)pageResult.getItems();
        List<VideoItemsVo> videoItemsVos = new ArrayList<>();
        UserInfo userInfo = userInfoApi.findById(uid);
        String nickname = userInfo.getNickname();

        for (Video video :
                items) {
            VideoItemsVo videoItemsVo = new VideoItemsVo();
            BeanUtils.copyProperties(video,videoItemsVo);
            videoItemsVo.setId(uid.intValue());
            videoItemsVo.setUserId(uid.intValue());
            videoItemsVo.setCreateDate(video.getCreated().intValue());
            videoItemsVo.setNickname(nickname);
            videoItemsVos.add(videoItemsVo);
        }
        pageResult.setItems(videoItemsVos);

        return ResponseEntity.ok(pageResult);
    }

    public ResponseEntity<Object> getUserMessagesPage(Integer page, Integer pagesize, Long uid,String state) {
        PageResult pageResult = publishApi.findAlbum(page, pagesize, uid);
        List<Publish> items = (List<Publish>)pageResult.getItems();
        List<MMovementsVo> mMovementsVos = new ArrayList<>();
        UserInfo userInfo = userInfoApi.findById(uid);

        for (Publish publish :
                items) {
            MMovementsVo mMovementsVo = new MMovementsVo();
            BeanUtils.copyProperties(publish,mMovementsVo);
            mMovementsVo.setId(publish.getId().toString());
            mMovementsVo.setCreateDate(publish.getCreated().intValue());
            mMovementsVo.setImageContent(publish.getMedias().toArray(new String[]{}));
            mMovementsVo.setState(state);
            mMovementsVo.setUserId(uid.intValue());
            mMovementsVo.setAvatar(userInfo.getAvatar());
            mMovementsVo.setNickname(userInfo.getNickname());
            mMovementsVos.add(mMovementsVo);
        }
        pageResult.setItems(mMovementsVos);
        return ResponseEntity.ok(pageResult);
    }

    public ResponseEntity<Object> getUserMessageDetail(String publishId) {
        return null;
    }
}
