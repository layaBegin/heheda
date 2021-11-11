package com.tanhua.domain.mongo.db;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "recommend_user")
public class RecommendUser implements java.io.Serializable {
    private static final long serialVersionUID = 5874126532504390567L;
    @Id //主键id
    private ObjectId id; //主键id 。mongo 主键id都是ObjectId 的格式
    @Indexed
    private Long recommendUserId; //推荐的用户id
    private Long userId; //用户id
    @Indexed
    private Double score =0d; //推荐得分
    private String date; //日期
}