package com.tanhua.commons.config;

import com.tanhua.commons.properties.AipFaceProperties;
import com.tanhua.commons.properties.HuanXinProperties;
import com.tanhua.commons.properties.OssProperties;
import com.tanhua.commons.properties.SmsProperties;
import com.tanhua.commons.templates.AipFaceTemplate;
import com.tanhua.commons.templates.HuanXinTemplate;
import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.commons.templates.SmsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*
部署自动装配类步骤：
1，yml 文件中配置自动装配 属性
2，创建SmsProperties文件并自动读取yml中的属性
3，创建自动装配类TanhuaAutoConfiguration，并在resources文件夹中配置spring.factories
目的：只是让SmsTemplate 自动配置了server模块中的yml中配置的sms各属性，做到解耦
 */
@Configuration
//自动的读取yml中配置信息，并赋值到SmsProperties、OssProperties对象中，将此对象存入容器
@EnableConfigurationProperties({
        SmsProperties.class,
        OssProperties.class,
        AipFaceProperties.class,
        HuanXinProperties.class
})
public class TanhuaAutoConfiguration {

    /*
    1,@Bean 修饰方法自动把方法返回的对象加入容器
    2，方法参数：自动去容器中查找对应类型的对象注入到该方法的参数中，
                如果该类型在容器中有多个对象，会根据参数名查找
     */
    @Bean
    public SmsTemplate smsTemplate(SmsProperties smsProperties) {
        SmsTemplate smsTemplate = new SmsTemplate(smsProperties);
        return smsTemplate;
    }

    @Bean
    public OssTemplate ossTemplate(OssProperties ossProperties){
        OssTemplate ossTemplate = new OssTemplate(ossProperties);
        return ossTemplate;
    }
    @Bean
    public AipFaceTemplate aipFaceTemplate(AipFaceProperties properties) {
        return new AipFaceTemplate(properties);
    }

    @Bean
    public HuanXinTemplate huanXinTemplate(HuanXinProperties properties) {
        return new HuanXinTemplate(properties);
    }
}