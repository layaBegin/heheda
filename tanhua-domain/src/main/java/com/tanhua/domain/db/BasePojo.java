package com.tanhua.domain.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/*
自动填充分2步：
1，加上@TableField(fill = FieldFill.xxx)注解
2，编写MetaObjectHandler 填充处理器
 */
@Data
public abstract class BasePojo implements Serializable {

    //TableField 在insert 的时候会自动填充
    @TableField(fill = FieldFill.INSERT)
    private Date created;
    //TableField 在insert或update的时候会自动注入
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updated;
}
