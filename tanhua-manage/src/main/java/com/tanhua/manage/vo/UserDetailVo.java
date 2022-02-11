package com.tanhua.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailVo implements Serializable {
    private Integer id;
    private String nickname;
    private String avatar;
    private String gender;
    private Integer age;
    private Integer income;
    private String state;//用户状态,1为正常，2为冻结
    private Integer created;
    private String city;
}
