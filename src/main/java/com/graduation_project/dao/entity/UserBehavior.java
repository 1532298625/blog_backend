package com.graduation_project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("user_behavior")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBehavior implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    private Long userid;
    private String articleid = "";
    private double count;
}
