package com.tanhua.server.test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    public static void main(String[] args) {
        // 1、定义要加密的数据
        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        map.put("mobile","15900110011");

        // 2、定义密钥
        String secret = "itcast";

        // 3、生成token
        String token = Jwts.builder()
                .setClaims(map) // 声明加密的数据
                .signWith(SignatureAlgorithm.HS256, secret) // 指定加密算法与密钥
                .compact();

        System.out.println("==token:"+token);
        // eyJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiIxNTkwMDExMDAxMSIsImlkIjoxfQ.ERBf0_OjT_UmTqcbOGRYOXGj4HvOlkP2UqIPGg9MAVc


        // 4、根据token，解析数据，获取map集合
        Map<String,Object> body =
                (Map<String, Object>) Jwts.parser().setSigningKey(secret).parse(token).getBody();
        // {mobile=15900110011, id=1}
        System.out.println(body);
    }
}