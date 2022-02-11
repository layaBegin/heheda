package com.tanhua.manage.service;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.manage.Utils.AdminHolder;
import com.tanhua.manage.Utils.JwtUtils;
import com.tanhua.manage.api.AdminApi;
import com.tanhua.manage.exception.BusinessException;
import com.tanhua.manage.mapper.AdminMapper;
import com.tanhua.manage.pojo.Admin;
import com.tanhua.manage.vo.AdminVo;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AdminService extends ServiceImpl<AdminMapper,Admin> {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private AdminApi adminApi;
    @Value("${tanhua.secret}")//从配置文件里取值
    private String secret;

    private final String TOKEN_KEY = "MANAGE_TOKEN_KEY_";
    public void saveCap(String uuid, String code) {
        System.out.println("===code:"+code);
        //缓存验证码
        redisTemplate.opsForValue().set("MANAGE_CODE_" + uuid,code,Duration.ofMinutes(2));
    }

    public ResponseEntity<Object> login(String username, String password, String verificationCode, String uuid) throws Exception {

        String key = redisTemplate.opsForValue().get("MANAGE_CODE_" + uuid);
        if (!verificationCode.equalsIgnoreCase(key)){
            throw new BusinessException("验证码错误");
        }else {
            String passwordMD5 = DigestUtils.md5Hex(password);
            Admin admin = adminApi.getAdmin(username);
            if (admin != null && passwordMD5.equalsIgnoreCase(admin.getPassword())){
                //获取token
                String token = JwtUtils.createToken(username, secret);
                String sAdmin = JSON.toJSONString(admin);
                //将token 和用户存入redis；
                redisTemplate.opsForValue().set(TOKEN_KEY + token,sAdmin,Duration.ofMinutes(300));
                redisTemplate.delete(key);
                Map<String,String> result = new HashMap<>();
                result.put("token",token);
                return ResponseEntity.ok(result);
            }else {
                throw new BusinessException("用户名或密码错误");

            }
        }
    }


    public Admin findByToken(String token) {
        token = token.replace("Bearer ","");
        String key = TOKEN_KEY + token;
        String jsonAdmin = redisTemplate.opsForValue().get(key);
        if (jsonAdmin == null){
            throw  new BusinessException("token校验异常");
        }
        Admin admin = JSON.parseObject(jsonAdmin, Admin.class);
        //更新token有效时间
        redisTemplate.expire(key,4, TimeUnit.HOURS);

        return admin;
    }

    public ResponseEntity<Object> getProfile() {
        Admin admin = AdminHolder.get();
        AdminVo adminVo = new AdminVo();
        BeanUtils.copyProperties(admin,adminVo);
        adminVo.setId(admin.getId());
        if (adminVo.getAvatar() == null){
            adminVo.setAvatar("");
        }
        return ResponseEntity.ok(adminVo);
    }

    public ResponseEntity<Object> logout(String token) {
        redisTemplate.delete(TOKEN_KEY + token);
        return ResponseEntity.ok(null);
    }
}
