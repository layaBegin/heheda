package com.tanhua.manage.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisByDay extends BasePojo {

    private Long id;
    private Date recordDate;//日期
    private Integer numRegistered;//注册数量
    private Integer numActive;//活跃用户数
    private Integer numLogin;//登录用户
    private Integer numRetention1d;//次日留存用户：昨天注册，今天登录


}
