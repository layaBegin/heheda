package com.tanhua.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoItemsVo implements Serializable {

    private Integer id;
    private String nickname;
    private Integer userId;
    private Integer createDate;
    private String videoUrl;
    private String picUrl;
    private Integer likeCount;
    private Integer commentCount;
}
