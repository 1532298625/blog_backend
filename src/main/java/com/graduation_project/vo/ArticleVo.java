package com.graduation_project.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.graduation_project.dao.entity.Category;
import com.graduation_project.dao.entity.Tags;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class ArticleVo {
    private String id;

    // 标题
    private String title;

    // 简介
    private String summary;

    // md格式内容
    private String content;

    // html格式
    private String contentHtml;

    // 是否置顶  默认 0 不置顶
    private Integer weight;

    private Integer likenum;

    private String cover;

    // 评论数量  默认 0
    private Integer commentCounts;

    // 浏览数量  默认 0
    private double viewCounts;

    // 创建时间
     @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    // 作者id
    private UserVo user;

    //分类
    private List<Category> categoryList;

    //标签
    private List<Tags> tagsList;

}
