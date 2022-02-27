package com.tanhua.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageCommentVo implements Serializable {
    private String id;
    private String nickname;
    private Integer userId;
    private String content;
    private Integer createDate;
}
