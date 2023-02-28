package com.graduation_project.vo.params;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.graduation_project.dao.entity.Category;
import com.graduation_project.vo.UserVo;
import lombok.Data;

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
public class ArticleParams implements Serializable {
        @TableId(type = IdType.ASSIGN_ID)
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

        // 评论数量  默认 0
        private Integer commentCounts;

        private String cover;

        // 浏览数量  默认 0
        private Integer viewCounts;

        // 创建时间
        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private Date createDate;

        // 更新时间
        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;

        // 作者id
        private UserVo user;

        //分类
        private List<String> categoryId;

        //标签
        private List<String> tagsId;
}



