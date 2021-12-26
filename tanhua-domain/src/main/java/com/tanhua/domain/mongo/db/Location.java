package com.tanhua.domain.mongo.db;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "location")
//为了提高查询效率，设置复合索引，2dsphere = 2D球面
@CompoundIndex(name = "location_index", def = "{'location': '2dsphere'}")
public class Location implements Serializable {
    private static final long serialVersionUID = -8296531091587438527L;
    @Id
    private ObjectId id;
    @Indexed
    private Long userId; //用户id
    private Long created; //创建时间
    private GeoJsonPoint location;
    private String address;
    private Long updated;//更新时间
    private Long lastUpdated;//上次更新时间

}
