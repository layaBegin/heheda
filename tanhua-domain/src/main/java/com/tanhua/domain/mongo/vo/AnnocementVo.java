package com.tanhua.domain.mongo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnocementVo implements Serializable {

    private String id;
    private String title;
    private String description;
    private String createDate;

}
