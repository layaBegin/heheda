package com.tanhua.server.service;

import com.alibaba.fastjson.JSON;
import com.tanhua.commons.templates.AipFaceTemplate;
import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.commons.templates.SmsTemplate;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.ErrorResult;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.domain.db.User;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.server.utils.JwtUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 注意：这里导入spring的包: org.springframework.stereotype.Service
 * 因为不是dubbo服务
 */
@Service
public class UserService {

    @Reference
    private UserApi userApi;
    @Reference
    private UserInfoApi userInfoApi;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private AipFaceTemplate aipFaceTemplate;
    @Autowired
    private OssTemplate ossTemplate;

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
    public ResponseEntity<Object> loginReginfo(UserInfo userInfo,String token){
        //1,校验token
        User user = findByToken(token);
        if (user == null){
            return ResponseEntity.status(500).body(ErrorResult.error());
        }
        Long id = user.getId();
        userInfo.setId(id);
        userInfoApi.save(userInfo);
        return ResponseEntity.ok(null);
    }

    public ResponseEntity<Object> updateHead(MultipartFile headPhoto, String token) throws IOException {
        //1.检测token
        User user = findByToken(token);
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


}