package com.tanhua.dubbo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * dao接口如果需要被映射到需要使用一下两种方法：
 * 1. 在dao接口的添加@Mapper注解， 这种方式弊端：每一个dao接口都需要添加该注解非常繁琐
 * 2. 在启动器类添加一个MapperScan注解，指定扫描的dao包、
 */
//启动类
@SpringBootApplication
@MapperScan("com.tanhua.dubbo.mapper")//指定扫描的dao包
public class DubboServerApplication {
    public static void main(String[] args) {

        SpringApplication.run(DubboServerApplication.class, args);
    }
}
