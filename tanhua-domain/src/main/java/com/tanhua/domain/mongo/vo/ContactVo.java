package com.tanhua.domain.mongo.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactVo {
    private Integer id;
    private String userId;//用户id
    private String avatar; //用户头像
    private String nickname; //昵称
    private String gender; //性别
    private Integer age; //年龄
    private String city; //城市

}
