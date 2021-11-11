package com.tanhua.domain.mongo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RrecommendationResponseVo implements Serializable {

    private Integer counts;//总条数
    private Integer pagesize;//每页条数
    private Integer pages; //总页数
    private List<TodayBestVo> items; //每条的内容
}
