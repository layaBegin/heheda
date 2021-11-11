package com.tanhua.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;


//TanhuaServer启动会报错，原因分析：springboot就会自动去连接mongo，由于他连接不上会导致出错。
@SpringBootApplication(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
public class TanhuaServerApplication {
    public static void main(String[] args) {

        SpringApplication.run(TanhuaServerApplication.class,args);
    }
}