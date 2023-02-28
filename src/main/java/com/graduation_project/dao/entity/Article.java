package com.graduation_project.dao.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (Article)表实体类
 *
 * @author makejava
 * @since 2022-05-24 13:34:18
 */
@Data
@TableName("article")
public class Article implements Serializable {
    private String id;

    // 标题
    private String title;

    // 简介
    private String summary;
    //封面
    private String cover;

    // md格式内容
    private String content;

    // html格式
    private String contentHtml;

    // 是否置顶  默认 0 不置顶
    @TableField(fill = FieldFill.INSERT)
    private Integer weight;

    @TableField(fill = FieldFill.INSERT)
    private Integer likenum;
    // 评论数量  默认 0
    @TableField(fill = FieldFill.INSERT)
    private Integer commentCounts;

    // 浏览数量  默认 0
    @TableField(fill = FieldFill.INSERT)
    private double viewCounts;

    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    // 作者id
    private Long authorId;

    // 删除标志（0代表未删除，1代表已删除）
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;


    //分类
    @TableField(exist = false)
    private List<Category> categoryList;

    //标签
    @TableField(exist = false)
    private List<Tags> tagsList;
}


