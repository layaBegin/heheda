package com.tanhua.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageVo implements Serializable {

    private Integer id;
    private String avatar;
    private String nickname;
    private String gender;
    private Integer age;
    private String state;//用户状态,1为正常，2为冻结
    private String city;


}
