package com.tanhua.server.test;

import com.tanhua.commons.properties.OssProperties;
import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.commons.templates.SmsTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OssTest {
    @Autowired
    OssTemplate ossTemplate;

    @Test
    public void testUpload() throws FileNotFoundException {
        String file = "E:\\hehe.jpg";
        FileInputStream fileInputStream = new FileInputStream(file);
        String url = ossTemplate.upload(file, fileInputStream);
        System.out.println("upload:"+url);
    }
}
