package com.tanhua.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MMovementsVo implements Serializable {
    private String id;
    private String nickname;
    private Integer userId;
    private String avatar;
    private Integer createDate;
    private String textContent;
    private String state;//审核状态，1为待审核，2为自动审核通过，3为待人工审核，4为人工审核拒绝，5为人工审核通过，6为自动审核拒绝
    private Integer likeCount;
    private Integer commentCount;
    private String[] imageContent;

}
