package com.graduation_project.dao.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Suggest implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userid;

    private String username;

    private String address;

    private String title;

    private String content;

    private Integer phonenumber;


    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    // 删除标志（0代表未删除，1代表已删除）
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;
}
