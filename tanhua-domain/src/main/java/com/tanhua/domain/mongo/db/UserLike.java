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
@Document(collection = "user_like")
public class UserLike implements Serializable {

    private static final long serialVersionUID = 5580738201456130952L;
    private ObjectId id;
    private Long userId;
    private Long likeUserId;
    private Long created;
}
