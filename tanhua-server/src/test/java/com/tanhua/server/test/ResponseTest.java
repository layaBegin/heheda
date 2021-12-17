package com.tanhua.server.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ResponseTest {

    @Test
    public void test1(){
        Map<String,Object> map = new HashMap<>();
        map.put("verfication",true);
        String s1 = JSON.toJSONString(map);
        System.out.println(s1);
    }



}
