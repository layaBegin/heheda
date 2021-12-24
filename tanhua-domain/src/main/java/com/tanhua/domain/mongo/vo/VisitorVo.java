package com.tanhua.domain.mongo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitorVo implements Serializable {

    private Integer id;//用户id
    private String avatar;
    private String nickname;
    private String gender; //man,woman
    private Integer age;
    private String[] tags;
    private Integer fateValue;
}
