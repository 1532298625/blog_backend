package com.graduation_project.dao.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表(User)表实体类
 *
 * @author makejava
 * @since 2022-05-23 18:43:11
 */
@Data
@TableName("user")
public class User implements Serializable {

    // 主键

    @JsonSerialize(using = ToStringSerializer.class)//解决long精度丢失问题
    private Long id;
    
    // 昵称
    private String nickName;
    
    // 用户名
    private String userName;
    
    // 密码
    private String password;
    
    // 头像
    private String avatar;
    
    // 用户性别（0男，1女，2未知） 默认 2
    @TableField(fill = FieldFill.INSERT)
    private String sex;
    
    // 账号状态（0正常 1停用） 默认 0
    @TableField(fill = FieldFill.INSERT)
    private String status;
    
    // 邮箱
    private String email;
    
    // 手机号
    private String phonenumber;
    
    // 用户类型（0管理员，1普通用户） 默认 1
    @TableField(fill = FieldFill.INSERT)
    private String userType;
    
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //验证码
    @TableField(exist = false)
    private String code;
    
    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    
    // 删除标志（0代表未删除，1代表已删除）
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;
    
}


