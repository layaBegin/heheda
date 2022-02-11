package com.tanhua.manage.api.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.tanhua.manage.api.AnalysisByDayApi;
import com.tanhua.manage.mapper.AnalysisByDayMapper;
import com.tanhua.manage.pojo.AnalysisByDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalysisByDayApiImpl implements AnalysisByDayApi {

    @Autowired
    private AnalysisByDayMapper analysisByDayMapper;

    public Integer cumulativeUsers() {
        QueryWrapper<AnalysisByDay> queryWrapper = new QueryWrapper<>();
        QueryWrapper<AnalysisByDay> select = queryWrapper.select("SUM(num_registered) as numRegistered");
        AnalysisByDay analysisByDay = analysisByDayMapper.selectOne(select);
        return analysisByDay.getNumRegistered();
    }


}
