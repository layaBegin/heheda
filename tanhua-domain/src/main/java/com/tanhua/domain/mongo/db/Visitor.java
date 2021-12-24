package com.tanhua.domain.mongo.db;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "visitor")
public class Visitor implements Serializable {

    private static final long serialVersionUID = -6143568288050602217L;
    private ObjectId id;
    private Long userId;
    private Long visitorId;
    private Long date;
    private String from; // 访问哪里


    private Integer fateValue;

}
