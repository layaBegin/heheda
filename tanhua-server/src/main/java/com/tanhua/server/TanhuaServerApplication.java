package com.tanhua.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;


//TanhuaServer启动会报错，原因分析：springboot就会自动去连接mongo，由于他连接不上会导致出错。
//所有排除下面两个自动配置文件MongoAutoConfiguration，MongoDataAutoConfiguration
@SpringBootApplication(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
@EnableCaching // 开启spring的注解缓存支持
public class TanhuaServerApplication {
    public static void main(String[] args) {

        SpringApplication.run(TanhuaServerApplication.class,args);
    }
}