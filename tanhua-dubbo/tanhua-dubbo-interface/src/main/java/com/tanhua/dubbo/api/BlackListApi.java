package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tanhua.domain.db.BlackList;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.PageResult;

import java.util.List;

public interface BlackListApi {
    IPage<UserInfo> blackList(Long id, Integer page, Integer pagesize);

    void deleteBlackByUid(Long id,Long bUid);
}
