package com.tanhua.dubbo.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.domain.db.Question;
import com.tanhua.dubbo.api.QuestionApi;
import com.tanhua.dubbo.mapper.QuestionMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class QuestionApiImpl implements QuestionApi {
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public Question findQuestionByUserId(Long id) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",id);
        return questionMapper.selectOne(wrapper);
    }

    @Override
    public int save(Question question) {
        int count = questionMapper.insert(question);
        return count;
    }

    @Override
    public int update(Question question) {
        int count = questionMapper.updateById(question);
        return count;
    }
}
