package com.tanhua.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * dao接口如果需要被映射到需要使用一下两种方法：
 * 1. 在dao接口的添加@Mapper注解， 这种方式弊端：每一个dao接口都需要添加该注解非常繁琐
 * 2. 在启动器类添加一个MapperScan注解，指定扫描的dao包、
 */
//TanhuaServer启动会报错，原因分析：springboot就会自动去连接mongo，由于他连接不上会导致出错。
//所有排除下面两个自动配置文件MongoAutoConfiguration，MongoDataAutoConfiguration
@SpringBootApplication(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
@MapperScan("com.tanhua.manage.mapper")//指定扫描的dao包
public class ManageServerApplication {
    public static void main(String[] args) {

        SpringApplication.run(ManageServerApplication.class, args);
    }
}
