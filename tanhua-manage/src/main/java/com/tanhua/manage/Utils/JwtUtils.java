package com.tanhua.manage.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    public static String createToken(String username,String secret){
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, secret).setClaims(map).compact();
        return token;
    }
}
