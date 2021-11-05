package com.tanhua.dubbo.test;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.domain.db.User;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.mapper.UserInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserInfoMapperTest {

    /**
     * 测试dubbo服务：
     * 1、在dubbo服务工程中，测试dubbo服务，注入dubbo接口代理对象，使用@Autowired注入
     * 2、在服务消费者工程中注入dubbo服务使用@Reference
     * 3、注意：运行当前测试类时候，不要启动dubbo服务，否则报错。
     * 4、如果注入的userApi对象报错，可能是idea校验的问题，不用理会。
     */
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    public void testPage() {
        //参数1：当前页，参数2：页大小
        IPage<UserInfo> page = new Page<>(1,2);
        userInfoMapper.selectPage(page,null);
        System.out.println("current :"+page.getCurrent());
        System.out.println("getTotal :"+page.getTotal());
        System.out.println("getPages :"+page.getPages());
        System.out.println("getRecords :"+page.getRecords());
        System.out.println("getSize :"+page.getSize());
    }


}
