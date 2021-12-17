package com.tanhua.domain.mongo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {

    private String id;    //评论id
    private String avatar; //头像
    private String nickname; //头像
    private String content;
    private String createDate;
    private Integer likeCount = 0;//这条评论的点赞数
    private Integer hasLiked = 0  ;//（1是，0否），自己是否为这条评论点赞了
}
