package com.tanhua.server.test;

import com.tanhua.commons.templates.SmsTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsTest {
    @Autowired
    SmsTemplate smsTemplate;

    @Test
    public void testSend(){
        smsTemplate.sendSms("18675849881","1234");
    }
}
