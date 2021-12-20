package com.tanhua.domain.mongo.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoVo implements Serializable {

    private String id; //主键id

    private Integer userId;
    private String avatar; //
    private String nickname; //视频封面文件，URL
    private Integer hasFocus = 0; //是否关注 （1是，0否）
    private String  cover ;//封面
    private String  videoUrl;
    private String signature;//签名
    private Integer likeCount=0; //点赞数
    private Integer hasLiked=0; //是否已点赞 （1是，0否）
    private Integer commentCount=0; //评论数量
}
