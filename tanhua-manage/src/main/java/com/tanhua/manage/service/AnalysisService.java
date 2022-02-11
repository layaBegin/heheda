package com.tanhua.manage.service;

import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.manage.Utils.AdminHolder;
import com.tanhua.manage.Utils.ComputeUtil;
import com.tanhua.manage.api.impl.AnalysisByDayApiImpl;
import com.tanhua.manage.mapper.AnalysisByDayMapper;
import com.tanhua.manage.pojo.Admin;
import com.tanhua.manage.pojo.AnalysisByDay;
import com.tanhua.manage.vo.AnalysisSummaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class AnalysisService extends ServiceImpl<AnalysisByDayMapper, AnalysisByDay> {

    @Autowired
    private AnalysisByDayApiImpl analysisByDayApi;
    @Autowired
    private AnalysisByDayMapper analysisByDayMapper;

    public ResponseEntity<Object> getSummary() {
        AnalysisSummaryVo analysisSummaryVo = new AnalysisSummaryVo();
        //SELECT SUM(num_registered ) num_registered FROM tb_analysis_by_day
        //两种方法都行，也可以在ApiImpl 里面写
        AnalysisByDay one = query().select("SUM(num_registered) as numRegistered").one();
        Integer cumulativeUsers = one.getNumRegistered();

        Date date = new Date();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(date);
        //SELECT num_registered,num_login,num_active FROM tb_analysis_by_day WHERE record_date='2022-02-08'
        AnalysisByDay record_today = query().eq("record_date", today).one();
        Integer numRegistered = 0;
        Integer numLogin = 0 ;
        Integer numActive = 0;
        if(record_today != null){
            numRegistered = record_today.getNumRegistered();
            numLogin = record_today.getNumLogin();
            numActive = record_today.getNumActive();
        }
        //查询昨日新增
        //SELECT num_registered,num_login,num_active FROM tb_analysis_by_day WHERE record_date='2022-02-07';
        String yestoday = ComputeUtil.offsetDay(date,-1);
        AnalysisByDay record_yestoday = query().eq("record_date", yestoday).one();
        Integer numRegistered1 = 0;
        Integer numActive1 = 0;
        Integer numLogin1 = 0;
        if (record_yestoday != null){
             numRegistered1 = record_yestoday.getNumRegistered();
             numActive1 = record_yestoday.getNumActive();
             numLogin1 = record_yestoday.getNumLogin();
        }


        BigDecimal numRegisteredRate = ComputeUtil.computeRate(numRegistered, numRegistered1);
        BigDecimal numActiveRate = ComputeUtil.computeRate(numActive, numActive1);
        BigDecimal numLoginRate = ComputeUtil.computeRate(numLogin, numLogin1);

        //过去7、30天活跃用户
        AnalysisByDay record_7 = query().between("record_date", ComputeUtil.offsetDay(date, -7), today).select("SUM(num_active) AS num_active").one();
        AnalysisByDay record_30 = query().between("record_date", ComputeUtil.offsetDay(date, -30), today).select("SUM(num_active) AS num_active").one();

        analysisSummaryVo.setCumulativeUsers(cumulativeUsers.longValue());
        analysisSummaryVo.setNewUsersToday(numRegistered.longValue());
        analysisSummaryVo.setActiveUsersToday(numActive.longValue());
        analysisSummaryVo.setLoginTimesToday(numLogin.longValue());
        analysisSummaryVo.setActivePassMonth(record_30.getNumActive().longValue());
        analysisSummaryVo.setActivePassWeek(record_7.getNumActive().longValue());
        analysisSummaryVo.setActiveUsersTodayRate(numActiveRate);
        analysisSummaryVo.setNewUsersTodayRate(numLoginRate);
        analysisSummaryVo.setLoginTimesTodayRate(numRegisteredRate);
        return ResponseEntity.ok(analysisSummaryVo);
    }
}
