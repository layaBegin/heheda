package com.tanhua.server.service;


import com.alibaba.fastjson.JSON;
import com.tanhua.commons.templates.HuanXinTemplate;
import com.tanhua.domain.db.Question;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.db.Comment;
import com.tanhua.domain.mongo.db.Friend;
import com.tanhua.domain.mongo.db.Publish;
import com.tanhua.domain.mongo.vo.ContactVo;
import com.tanhua.domain.mongo.vo.LikesVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.QuestionApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.mongo.CommentApi;
import com.tanhua.dubbo.api.mongo.FriendApi;
import com.tanhua.dubbo.api.mongo.PublishApi;
import com.tanhua.server.utils.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class IMService {
    @Autowired
    private HuanXinTemplate huanXinTemplate;
    @Reference
    private UserInfoApi userInfoApi;
    @Reference
    private QuestionApi questionApi;
    @Reference
    private FriendApi friendApi;
    @Reference
    private CommentApi commentApi;
    @Reference
    private PublishApi publishApi;

    public ResponseEntity<Object> replyQuestions(Integer userId, String reply) {
        //1. 查询当前登陆用户信息
        UserInfo userInfo = userInfoApi.findById(UserHolder.getUserId());
        //2. 根据陌生人用户id查询问题
        Question question = questionApi.findQuestionByUserId(userId.longValue());

        //3. 构建消息内容 (格式固定)
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userInfo.getId());
        map.put("huanXinId",userInfo.getId());
        map.put("nickname", userInfo.getNickname());
        map.put("strangerQuestion", question==null?"你喜欢我吗.？":question.getTxt());
        map.put("reply", reply);
        //4. map转换为json字符串，并实现发消息
        String result = JSON.toJSONString(map);
        //5. 发消息
        huanXinTemplate.sendMsg(userId.toString(),result);
        return ResponseEntity.ok(null);
    }

    public ResponseEntity<Object> addContacts(Integer friendId) {
        Long userId = UserHolder.getUserId();
        friendApi.save(userId,friendId);
        //2. 好友关系注册到环信
        huanXinTemplate.contactUsers(UserHolder.getUserId(),(long)friendId);
        return ResponseEntity.ok(null);
    }

    public ResponseEntity<Object> deleteContacts(Long friendId) {
        Long userId = UserHolder.getUserId();
        friendApi.delete(userId,friendId);
        //2. 删除到环信
        huanXinTemplate.deleteUsers(UserHolder.getUserId(),friendId);
        return ResponseEntity.ok(null);
    }

    public PageResult getContantList(Integer page, Integer pagesize, String keyword) {
        Long userId = UserHolder.getUserId();
        PageResult pageResult = friendApi.getContantPage(page,pagesize,userId);
        List<Friend> friendsItems = (List<Friend>)pageResult.getItems();
        List<ContactVo> contactVoList = new ArrayList<>();
        for (Friend f :
                friendsItems) {
            Long friendId = f.getFriendId();
            UserInfo userInfo = userInfoApi.findById(friendId);
            ContactVo contactVo = new ContactVo();
            BeanUtils.copyProperties(userInfo,contactVo);
            contactVo.setId(userId.intValue());
            contactVo.setUserId(String.valueOf(friendId));
            contactVoList.add(contactVo);
        }
        pageResult.setItems(contactVoList);
        return pageResult;
    }

    public PageResult getLikesList(Integer page, Integer pagesize,Integer commentType) {
        Long userId = UserHolder.getUserId();
        PageResult pageResult = commentApi.findListByPuserId(page,pagesize,userId,commentType);
        List<Comment> items = (List<Comment>)pageResult.getItems();
        List<LikesVo> likesVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(items)){
            for (Comment comment :
                    items) {

                LikesVo likesVo = new LikesVo();
                likesVo.setId(comment.getUserId().toString());
                UserInfo userInfo = userInfoApi.findById(comment.getUserId());

                likesVo.setAvatar(userInfo.getAvatar());
                likesVo.setNickname(userInfo.getNickname());
                likesVo.setCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(comment.getCreated()));
                likesVoList.add(likesVo);
            }
        }
        pageResult.setItems(likesVoList);
        return pageResult;
    }


}
