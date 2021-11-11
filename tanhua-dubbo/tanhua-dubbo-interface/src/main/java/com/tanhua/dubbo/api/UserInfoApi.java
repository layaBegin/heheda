package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tanhua.domain.db.User;
import com.tanhua.domain.db.UserInfo;

public interface UserInfoApi {
    /**
     * 保存用户信息
     * @param userInfo
     * @return
     */
    void save(UserInfo userInfo);

    void updateById(UserInfo userInfo);

    UserInfo findById(Long userId);

}
