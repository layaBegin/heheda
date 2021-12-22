package com.tanhua.server.test;

import com.alibaba.fastjson.JSON;
import com.tanhua.commons.templates.HuanXinTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTest {

//    public static void main(String[] args) {
//        getToken();
//    }

    //Rest 风格 获取环信token
    public static void getToken(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://a1.easemob.com/1115211221113776/demo/token";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("grant_type","client_credentials");
        requestMap.put("client_id","YXA6FKqxNS_7QBKQxwIye0LfKw");
        requestMap.put("client_secret","YXA6JRNo_3KNOmFhaNJEIyleX3kmv-Q");
        String request = JSON.toJSONString(requestMap);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        Map<Object, Object> map = JSON.parseObject(body, Map.class);
        System.out.println("==body:"+body);
        map.forEach((key,value)->{
            System.out.println(key + ":" + value);
        });

    }

    @Autowired
    private HuanXinTemplate huanXinTemplate;

    @Test
    public void testRegister(){
        huanXinTemplate.register(1L);
    }
}
