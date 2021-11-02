package com.tanhua.server.test.lombac;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder //给当前bean 提供构造者模式创建
public class Person {
    private Integer id;
    private String name;


}
