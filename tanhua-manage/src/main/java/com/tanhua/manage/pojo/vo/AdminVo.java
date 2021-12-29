package com.tanhua.manage.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminVo implements Serializable {

    private String uid;
    private String username;
    private String password;
    private String avatar;
}
