package com.tanhua.domain.mongo.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationVo implements Serializable {

    private Integer userId;
    private String avatar;
    private String nickname;

}
