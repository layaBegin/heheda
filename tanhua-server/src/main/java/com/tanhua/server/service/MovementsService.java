package com.tanhua.server.service;



import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.db.Comment;
import com.tanhua.domain.mongo.db.Publish;
import com.tanhua.domain.mongo.db.RecommendQuanzi;
import com.tanhua.domain.mongo.db.Visitor;
import com.tanhua.domain.mongo.vo.MovementsVo;
import com.tanhua.domain.mongo.vo.VisitorVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.mongo.CommentApi;
import com.tanhua.dubbo.api.mongo.PublishApi;
import com.tanhua.dubbo.api.mongo.VisitorApi;
import com.tanhua.server.utils.RelativeDateFormat;
import com.tanhua.server.utils.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MovementsService {
    @Autowired
    private OssTemplate ossTemplate;
    @Reference
    private PublishApi publishApi;
    @Reference
    private UserInfoApi userInfoApi;
    @Reference
    private VisitorApi visitorApi;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    /**
     * 接口名称：动态-发布
     */
    public ResponseEntity<Object> saveMovements(Publish publish, MultipartFile[] imageContent) throws IOException {
        //1. 获取登陆用户id
        Long userId = UserHolder.getUserId();

        //2. 处理文件上传，并封装上传的图片数组对象
        List<String> medias = new ArrayList<>();
        if (imageContent != null) {
            for (MultipartFile multipartFile : imageContent) {
                String url = ossTemplate.upload(
                        multipartFile.getOriginalFilename(), multipartFile.getInputStream());
                medias.add(url);
            }
        }

        //3. 设置参数
        publish.setMedias(medias);
        publish.setUserId(userId);

        //4. （发布）保存动态
        publishApi.save(publish);
        return ResponseEntity.ok(null);
    }

    /**
     * 接口名称：好友动态
     */
    public ResponseEntity<Object> queryPublishList(Integer page, Integer pagesize) {
        //1. 获取登陆用户id
        Long userId = UserHolder.getUserId();

        //2. 分页查询好友动态（自己的时间线表）
        PageResult pageResult = publishApi.findByTimeLine(page,pagesize,userId);
        List<Publish> publishList = (List<Publish>) pageResult.getItems();

        //3. 封装返回的结果MovementsVo集合
        //3.1 创建返回结果
        List<MovementsVo> voList = new ArrayList<>();
        //3.2 遍历查询的发布动态集合
        if (publishList != null) {
            for (Publish publish : publishList) {

                MovementsVo vo = this._getMovementsVo(publish);
                //3.2.4 对象添加到集合
                voList.add(vo);
            }
        }

        //4. 在把封装好的vo集合设置到分页对象中
        pageResult.setItems(voList);
        return ResponseEntity.ok(pageResult);
    }

    private MovementsVo _getMovementsVo(Publish publish){
        //3.2.1 创建vo对象
        MovementsVo vo = new MovementsVo();
        //3.2.2 封装数据：发布动态
        BeanUtils.copyProperties(publish,vo);
        //3.2.3 封装数据：先查询用户详情，再封装
        UserInfo userInfo = userInfoApi.findById(publish.getUserId());
        if (userInfo != null) {
            BeanUtils.copyProperties(userInfo,vo);
            if (userInfo.getTags() != null) {
                vo.setTags(userInfo.getTags().split(","));
            }
        }
        //3.2.3 封装数据：其他参数
        vo.setId(publish.getId().toString());
        vo.setUserId(publish.getUserId());
        vo.setImageContent(publish.getMedias().toArray(new String[]{}));
        vo.setDistance("50米");
        vo.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));
        String key = "public_like_comment_" + UserHolder.getUserId() + publish.getId().toString();
        String s = redisTemplate.opsForValue().get(key);
        int like = 0;
        if (s != null){
            like = 1;
        }
        String loveKey = "public_love_comment_" + UserHolder.getUserId() + publish.getId().toString();
        String sLove = redisTemplate.opsForValue().get(loveKey);
        int love = 0;
        if (sLove != null){
            love = 1;
        }
        vo.setHasLiked(like);
        vo.setHasLoved(love);
        return vo;
    }

    public ResponseEntity<Object> queryRecommend(Integer page, Integer pagesize) {
        //1. 获取登陆用户id
        Long userId = UserHolder.getUserId();
        PageResult result = publishApi.findRecommend(userId,page,pagesize);
        List<Publish> publishList = (List<Publish>)result.getItems();
        List<MovementsVo> movementsVoList = new ArrayList<>();
        for (Publish publish :
                publishList) {
            MovementsVo movementsVo = this._getMovementsVo(publish);
            if (movementsVo != null){
                movementsVoList.add(movementsVo);
            }
        }
        result.setItems(movementsVoList);
        return  ResponseEntity.ok(result);
    }

    public ResponseEntity<Object> queryOneMoveMent(String publishId) {
        Publish publish = publishApi.findOne(publishId);
        MovementsVo vo = this._getMovementsVo(publish);
        return ResponseEntity.ok(vo);
    }


    public ResponseEntity<Object> queryUserMovements(Integer page, Integer pagesize, Long userId) {
        if (userId == null){
            userId = UserHolder.getUserId();
        }
        PageResult result = publishApi.findAlbum(page,pagesize,userId);
        List<Publish> publishList = (List<Publish>)result.getItems();
        List<MovementsVo> movementsVoList = new ArrayList<>();
        for (Publish publish :
                publishList) {
            MovementsVo movementsVo = this._getMovementsVo(publish);
            if (movementsVo != null){
                movementsVoList.add(movementsVo);
            }
        }
        result.setItems(movementsVoList);
        return ResponseEntity.ok(result);
    }


    public ResponseEntity<Object> getVisitors() {
        Long userId = UserHolder.getUserId();
        String key = "lastLoginTime_" + userId;
        String date = redisTemplate.opsForValue().get(key);
        List<Visitor> list = null;
        if (date == null){
             list = visitorApi.getVisitors(userId,5);

        }else {
             list = visitorApi.getVisitors(userId,Long.parseLong(date));
        }
        redisTemplate.opsForValue().set(key,String.valueOf(System.currentTimeMillis()));

        List<VisitorVo> visitorVoList = new ArrayList<>();

        for (Visitor v :
                list) {
            VisitorVo visitorVo = new VisitorVo();
            visitorVo.setId(v.getVisitorId().intValue());
            UserInfo userInfo = userInfoApi.findById(v.getVisitorId());
            visitorVo.setAvatar(userInfo.getAvatar());
            visitorVo.setGender(userInfo.getGender());
            visitorVo.setNickname(userInfo.getNickname());
            visitorVo.setAge(userInfo.getAge());
            visitorVo.setTags(userInfo.getTags().split(","));
            visitorVo.setFateValue(v.getFateValue());
            visitorVoList.add(visitorVo);
        }
        return ResponseEntity.ok(visitorVoList);
    }

    public ResponseEntity<Object> setVisitors(Long id) {
        Long userId = UserHolder.getUserId();
        Visitor visitor = new Visitor();
        visitor.setUserId(userId);
        visitor.setVisitorId(id);
        visitor.setDate(System.currentTimeMillis());
        visitor.setFrom("首页");
        visitorApi.save(visitor);
        return null;
    }
}
