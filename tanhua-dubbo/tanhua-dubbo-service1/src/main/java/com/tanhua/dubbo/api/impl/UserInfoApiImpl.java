package com.tanhua.dubbo.api.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.domain.db.User;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.mapper.UserInfoMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Service dubbo服务提供者
 * 1、修饰dubbo服务类，给消费者远程调用
 * 2、导入dubbo提供的注解：org.apache.dubbo.config.annotation.Service
 * 3、不要导入spring的包：org.springframework.stereotype.Service
 */
@Service
public class UserInfoApiImpl implements UserInfoApi {

    @Autowired
    private UserInfoMapper userInfoMapper;



    @Override
    public void save(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    @Override
    public void updateById(UserInfo userInfo) {
        userInfoMapper.updateById( userInfo);
    }


    @Override
    public UserInfo findById(Long userId) {
        return userInfoMapper.selectById(userId);
    }


}
