package com.tanhua.server.test.cache;

import com.tanhua.domain.db.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class App {


    @Autowired
    private UserInfoTestService userInfoTestService;

    @Test
    public  void findAll(){
        List<UserInfo> all = userInfoTestService.findAll();
        System.out.println("listAll:"+all);
    }

    @Test
    public void update1(){
        userInfoTestService.update();
    }

    @Test
    public  void findById(){
        UserInfo byid = userInfoTestService.findByid(2L);
        System.out.println("userInfo:"+byid);
    }
    @Test
    public  void delete(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(2L);
        userInfoTestService.delete(userInfo);
    }
}
