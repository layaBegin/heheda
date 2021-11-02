package com.tanhua.server.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    public static String createToken(Long userId,String mobile,String secret){
        Map<String,Object> map = new HashMap<>();
        map.put("id",userId);
        map.put("mobile",mobile);
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, secret).setClaims(map).compact();
        return token;
    }
}
