package com.tanhua.dubbo.api.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.domain.db.BlackList;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.dubbo.api.BlackListApi;
import com.tanhua.dubbo.mapper.UserInfoMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class BlackListApiImpl implements BlackListApi {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public IPage<UserInfo> blackList(Long userId, Integer page, Integer pagesize) {
        IPage<UserInfo> iPage = new Page<>(page,pagesize);
        return userInfoMapper.blackList(iPage,userId);

    }

    @Override
    public void deleteBlackByUid(Long uid,Long bUid) {
        userInfoMapper.deleteBlackByUid(uid,bUid);
    }
}
