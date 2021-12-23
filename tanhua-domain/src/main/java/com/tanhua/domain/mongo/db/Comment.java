package com.tanhua.domain.mongo.db;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "quanzi_comment")
public class Comment  implements Serializable {

    private ObjectId id;
    private ObjectId publishId;    //发布id
    private Integer commentType;//评论类型，1-点赞，2-评论，3-喜欢
    private Integer pubType;//评论内容类型： 1-对动态操作 2-对视频操作 3-对评论操作
    private String content;
    private Integer likeCount;//这条评论的点赞数
    private Long puserId;//发布人
    private Long userId;//评论人
    private Long created;
    //动态选择更新的字段
    public String getCol() {
        return this.commentType == 1 ? "likeCount" : commentType==2? "commentCount"
                : "loveCount";
    }
}
