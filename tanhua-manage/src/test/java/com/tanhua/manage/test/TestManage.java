package com.tanhua.manage.test;

import com.tanhua.manage.api.AdminApi;
import com.tanhua.manage.api.impl.AdminApiImpl;
import com.tanhua.manage.mapper.AdminMapper;
import com.tanhua.manage.pojo.Admin;
import com.tanhua.manage.service.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestManage {

    @Autowired
    private AdminService adminService;

    @Test
    public void test1(){
        Admin admin = adminService.getById(1);
        System.out.println("==admin:"+admin);
    }


}
