package com.tanhua.server.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.spring.util.AnnotationUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tanhua.commons.templates.AipFaceTemplate;
import com.tanhua.commons.templates.HuanXinTemplate;
import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.commons.templates.SmsTemplate;
import com.tanhua.domain.db.Question;
import com.tanhua.domain.db.Settings;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.db.Friend;
import com.tanhua.domain.mongo.db.UserLike;
import com.tanhua.domain.mongo.db.Visitor;
import com.tanhua.domain.mongo.vo.FriendTypeVo;
import com.tanhua.domain.vo.ErrorResult;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.SettingsVo;
import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.dubbo.api.*;
import com.tanhua.domain.db.User;
import com.tanhua.dubbo.api.mongo.FriendApi;
import com.tanhua.dubbo.api.mongo.RecommendUserApi;
import com.tanhua.dubbo.api.mongo.UserLikeApi;
import com.tanhua.dubbo.api.mongo.VisitorApi;
import com.tanhua.server.utils.JwtUtils;
import com.tanhua.server.utils.UserHolder;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 注意：这里导入spring的包: org.springframework.stereotype.Service
 * 因为不是dubbo服务
 */
@Service
public class UserService {

    //用 dubbo 的Reference注解
    @Reference
    private UserApi userApi;
    @Reference
    private UserInfoApi userInfoApi;
    @Reference
    private SettingsApi settingsApi;
    @Reference
    private QuestionApi questionApi;
    @Reference
    private BlackListApi blackListApi;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private AipFaceTemplate aipFaceTemplate;
    @Autowired
    private OssTemplate ossTemplate;
    @Autowired
    private HuanXinTemplate huanXinTemplate;
    @Reference
    private FriendApi friendApi;
    @Reference
    private UserLikeApi userLikeApi;
    @Reference
    private VisitorApi visitorApi;
    @Reference
    private RecommendUserApi recommendUserApi;

    @Autowired
    private IMService imService;

    @Value("${tanhua.secret}")//从配置文件里取值
    private String secret;
    private final String SMS_KEY="SMS_KEY_";
    private final String TOKEN_KEY="TOKEN_KEY_";
    public ResponseEntity<Object> findByMobile(String mobile){
        User user = userApi.findByMobile(mobile);
        // 通过ResponseEntity往响应体中写数据
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<Object> save(User user){
        try {
            // 模拟异常
            //int i = 1/0;

            Long userId = userApi.save(user);
            // 正常返回
            return ResponseEntity.ok(userId);
        } catch (Exception e) {
            e.printStackTrace();
            // 异常返回：需要返回错误编码、错误信息
            Map<String, Object> resultMap = new HashMap<>();
            // 设置错误信息
            resultMap.put("errorCode",1000);
            resultMap.put("errMessage","对不起，我错了！");
            // 500 是http响应状态码，表示服务器异常
            return ResponseEntity.status(500).body(resultMap);
        }
    }

    public ResponseEntity<Object> login(@RequestBody Map<String,String> paramMap){

        String phone = paramMap.get("phone");
        String code = (int)((Math.random()*9 + 1)*100000)+"";
        //先注释掉，要花钱
        //smsTemplate.sendSms(phone,code);
        code = "123456";
        redisTemplate.opsForValue().set(SMS_KEY+phone,code, Duration.ofMinutes(5));//验证码有效时间为5分钟

        return ResponseEntity.ok(null);
    }

    //验证码校验
    public ResponseEntity<Object> loginVerification(@RequestBody Map<String,String> paramMap){
        String phone = paramMap.get("phone");
        String code = paramMap.get("verificationCode");
        String key = SMS_KEY + phone;
        String rCode = redisTemplate.opsForValue().get(key);
        if (rCode==null || !rCode.equalsIgnoreCase(code)){
            return ResponseEntity.status(500).body(ErrorResult.error());
        }else {
            boolean isNew = false;
            User user = userApi.findByMobile(phone);
            //没找到，自动注册
            if (user == null){
                user = new User();
                user.setMobile(phone);
                user.setPassword(DigestUtils.md5Hex("123456"));//默认密码123456，md5加密
               /* user.setCreated(new Date());
                user.setUpdated(new Date());*/
                Long id = userApi.save(user);
                user.setId(id);
                isNew = true;
                //新用户注册到环信
                huanXinTemplate.register(id);
            }
            String token = JwtUtils.createToken(user.getId(), phone, secret);
            //认证方案：token + redis
            String redisKey = TOKEN_KEY + token;
            String jsonUser = JSON.toJSONString(user);
            redisTemplate.opsForValue().set(redisKey,jsonUser,Duration.ofMinutes(30));
            Map<String,Object> map = new HashMap();
            map.put("token",token);
            map.put("isNew",isNew);
            return ResponseEntity.ok(map);
        }
    }

    //保存用户信息
    public ResponseEntity<Object> loginReginfo(UserInfo userInfo){
        //1,校验token
        User user = UserHolder.get();
        if (user == null){
            return ResponseEntity.status(500).body(ErrorResult.error());
        }
        Long id = user.getId();
        userInfo.setId(id);
        userInfoApi.save(userInfo);
        return ResponseEntity.ok(null);
    }

    public ResponseEntity<Object> updateHead(MultipartFile headPhoto) throws IOException {
        //1.检测token
        User user = UserHolder.get();
        if (user == null){
            return ResponseEntity.status(500).body(ErrorResult.error());
        }
        //2,人脸检测
        //boolean detect = aipFaceTemplate.detect(headPhoto.getBytes());
        //人脸识别不过，先设为true
        boolean detect = true;
        if (!detect){
            return ResponseEntity.status(500).body(ErrorResult.faceError());
        }
        //3,上传头像到阿里云
        String url = ossTemplate.upload(headPhoto.getOriginalFilename(), headPhoto.getInputStream());
        Long id = user.getId();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setAvatar(url);
        //4,更新userInfo
        userInfoApi.updateById(userInfo);
        return ResponseEntity.ok(null);
    }

    public User findByToken(String token) {
        String redisKey = TOKEN_KEY + token;
        String jsonUser = redisTemplate.opsForValue().get(redisKey);
        User user = JSON.parseObject(jsonUser, User.class);
        //刷新token
        if (user != null){
            redisTemplate.opsForValue().set(redisKey,jsonUser,Duration.ofMinutes(30));
            //redisTemplate.expire(redisKey,30, TimeUnit.MINUTES); 这个方法和上面效果一样
        }
        return user;
    }


    /**
     * 接口名称：用户资料 - 读取
     * @param
     * @return
     */
    public ResponseEntity<Object> findUserInfoById(Long userID,Long huanxinID) {
        User user = UserHolder.get();
        if (user == null){
            return ResponseEntity.status(500).body(ErrorResult.error());
        }
        Long userId = user.getId();
        //2. 如果userID不为NULL，根据用户id查询
        if (userID != null) {
            userId = userID;
        }else if(huanxinID != null){
            userId = huanxinID;
        }
        UserInfo userInfo = userInfoApi.findById(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        //对象拷贝
        BeanUtils.copyProperties(userInfo,userInfoVo);
        //类型不同，不会拷贝
        if (userInfo.getAge() != null){
            userInfoVo.setAge(userInfo.getAge().toString());
        }
        return ResponseEntity.ok(userInfoVo);
    }

    public ResponseEntity<Object> updateUserInfo(UserInfo userInfo) {
        User user = UserHolder.get();
        if (user == null){
            return ResponseEntity.status(500).body(ErrorResult.error());
        }
        Long id = user.getId();
        userInfo.setId(id);
        userInfoApi.updateById(userInfo);
        return ResponseEntity.ok(null);
    }

    public ResponseEntity<Object> querySettings() {
        User user = UserHolder.get();
        if (user == null){
            return ResponseEntity.status(500).body(ErrorResult.error());
        }
        Long id = user.getId();
        Settings settings = settingsApi.findSettingsByUserId(id);
        SettingsVo settingsVo = new SettingsVo();
        BeanUtils.copyProperties(settings,settingsVo);
        settingsVo.setPhone(user.getMobile());
        Question question = questionApi.findQuestionByUserId(id);
        settingsVo.setStrangerQuestion(question != null ? question.getTxt() : "你好，baby");
        return ResponseEntity.ok(settingsVo);
    }

    public ResponseEntity<Object> updateSettings(Settings param) {
        User user = UserHolder.get();
        if (user == null){
            return ResponseEntity.status(500).body(ErrorResult.error());
        }
        Settings setting = settingsApi.findSettingsByUserId(user.getId());
        if (setting != null){
            setting.setLikeNotification(param.getLikeNotification());
            setting.setPinglunNotification(param.getPinglunNotification());
            setting.setGonggaoNotification(param.getGonggaoNotification());
            settingsApi.update(setting);
        }else {
            setting = new Settings();
            BeanUtils.copyProperties(param,setting);
            setting.setUserId(user.getId());
            settingsApi.saveSettings(setting);
        }
        return ResponseEntity.ok(null);
    }

    public ResponseEntity<Object> saveQuestions(String content) {
        User user = UserHolder.get();
        if (user == null){
            return ResponseEntity.status(500).body(ErrorResult.error());
        }
        Long id = user.getId();
        Question question = questionApi.findQuestionByUserId(id);
        if (question != null){
            question.setTxt(content);
            questionApi.update(question);
        }else {
            question = new Question();
            question.setUserId(id);
            question.setTxt(content);
            questionApi.save(question);
        }

        return ResponseEntity.ok(null);
    }

    public ResponseEntity<Object> blacklist(Integer page, Integer pagesize) {
        User user = UserHolder.get();
        if (user == null){
            return ResponseEntity.status(500).body(ErrorResult.error());
        }
        Long id = user.getId();

        IPage<UserInfo> ipage = blackListApi.blackList(id, page, pagesize);

        PageResult pageResult = new PageResult(page,pagesize,(int)ipage.getTotal(),ipage.getRecords());

        return ResponseEntity.ok(pageResult);
    }

    public ResponseEntity<Object> deleteBlack(Long uid) {
        User user = UserHolder.get();
        if (user == null){
            return ResponseEntity.status(500).body(ErrorResult.error());
        }
        Long id = user.getId();
        blackListApi.deleteBlackByUid(id,uid);
        return ResponseEntity.ok(null);
    }

    public ResponseEntity<Object> queryFoucsCounts() {
        Long userId = UserHolder.getUserId();
        Integer eachLoveCount = friendApi.count(userId);
        Map<String,Integer> map = new HashMap<>();
        map.put("eachLoveCount",eachLoveCount);
        Integer loveCount =  userLikeApi.loveCount(userId);
        Integer fanCount =  userLikeApi.fanCount(userId);

        map.put("loveCount",loveCount);
        map.put("fanCount",fanCount);

        return ResponseEntity.ok(map);
    }

    public ResponseEntity<Object> alreadyLove(Long uid) {
        Long userId = UserHolder.getUserId();
        Boolean blove =  userLikeApi.alreadyLove(userId,uid);
        if (!blove){
            //是好友也让它点亮
            Boolean bFriend = friendApi.isFriend(userId,uid);
            blove = bFriend;
        }
        return ResponseEntity.ok(blove);
    }

    public ResponseEntity<Object> friendsTypeList(Integer type, Integer page, Integer pagesize) {
        Long userId = UserHolder.getUserId();
        PageResult pageResult = null;
        List<FriendTypeVo> friendTypeVoList = new ArrayList<>();
        if  (type == 1){//好友
             pageResult =  friendApi.getContantPage(page,pagesize,userId);
             List<Friend> items = (List<Friend>) pageResult.getItems();

            for (Friend f :
                    items) {
                UserInfo userInfo = userInfoApi.findById(f.getFriendId());
                FriendTypeVo friendTypeVo = new FriendTypeVo();
                BeanUtils.copyProperties(userInfo,friendTypeVo);
                friendTypeVo.setAlreadyLove(true);
                friendTypeVo.setId(userInfo.getId().intValue());
                Integer score =  recommendUserApi.queryScore(userId,f.getFriendId());
                friendTypeVo.setMatchRate(score);friendTypeVoList.add(friendTypeVo);
            }
        }else  if (type == 2){//我喜欢的
            pageResult =  userLikeApi.getLikePage(page,pagesize,userId);
            List<UserLike> items = (List<UserLike>)pageResult.getItems();
            for (UserLike userLike :
                    items) {
                FriendTypeVo friendTypeVo = new FriendTypeVo();
                UserInfo userInfo = userInfoApi.findById(userLike.getLikeUserId());
                BeanUtils.copyProperties(userInfo,friendTypeVo);
                friendTypeVo.setId(userLike.getLikeUserId().intValue());
                friendTypeVo.setAlreadyLove(true);
                Integer score =  recommendUserApi.queryScore(userId,userLike.getLikeUserId());
                friendTypeVo.setMatchRate(score);
                friendTypeVoList.add(friendTypeVo);
            }

        }else if (type == 3){//粉丝
            pageResult =  userLikeApi.getFansPage(page,pagesize,userId);
            List<UserLike> items = (List<UserLike>)pageResult.getItems();
            for (UserLike userLike :
                    items) {
                FriendTypeVo friendTypeVo = new FriendTypeVo();
                UserInfo userInfo = userInfoApi.findById(userLike.getUserId());
                BeanUtils.copyProperties(userInfo,friendTypeVo);
                friendTypeVo.setId(userLike.getUserId().intValue());
                friendTypeVo.setAlreadyLove(false);
                Integer score = recommendUserApi.queryScore(userId, userLike.getUserId());
                friendTypeVo.setMatchRate(score);
                friendTypeVoList.add(friendTypeVo);
            }
        }else if (type == 4){//谁看过我列表
            pageResult = visitorApi.getVisitorPage(page,pagesize,userId);
            List<Visitor> items = (List<Visitor>) pageResult.getItems();
            for (Visitor visitor :
                    items) {
                FriendTypeVo friendTypeVo = new FriendTypeVo();
                UserInfo userInfo = userInfoApi.findById(visitor.getVisitorId());
                BeanUtils.copyProperties(userInfo,friendTypeVo);
                friendTypeVo.setId(visitor.getVisitorId().intValue());
                friendTypeVo.setAlreadyLove(false);
                friendTypeVo.setMatchRate(visitor.getFateValue());
                Integer score = recommendUserApi.queryScore(userId, visitor.getVisitorId());
                friendTypeVo.setMatchRate(score);
                friendTypeVoList.add(friendTypeVo);
            }
        }
        if (pageResult != null){
            pageResult.setItems(friendTypeVoList);
        }

        return ResponseEntity.ok(pageResult);
    }


    //添加粉丝为好友
    public ResponseEntity<Object> loveFans(Long uid) {
        Long userId = UserHolder.getUserId();

        //1,好友表添加，环信注册好友
        imService.addContacts(uid.intValue());
        //2.like移除
        userLikeApi.delete(uid,userId);
        return ResponseEntity.ok(null);
    }

    //取消喜欢
    public ResponseEntity<Object> unLike(Long uid) {

        Long userId = UserHolder.getUserId();
        Boolean bFriend = friendApi.isFriend(userId, uid);
        if (bFriend){
            friendApi.delete(userId,uid);
            imService.deleteContacts(uid);
        }else {
            userLikeApi.delete(userId,uid);
        }
        return null;
    }
}