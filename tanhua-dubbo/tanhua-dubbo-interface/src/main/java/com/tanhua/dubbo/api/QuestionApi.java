package com.tanhua.dubbo.api;

import com.tanhua.domain.db.Question;
import com.tanhua.domain.db.Settings;

public interface QuestionApi {

    //查找用户setting资料
    Question findQuestionByUserId(Long id);

    int save(Question question);

    int update(Question question);
}
