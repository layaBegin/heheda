package com.tanhua.domain.mongo.db;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "recommend_quanzi")
public class RecommendQuanzi implements Serializable {

    private ObjectId id;
    private Long userId;
    private ObjectId publishId;
    private Double score = 0D;
    private Long created;
    private Long pid;

}
