package com.graduation_project.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * 文章的分类信息表
 */
@Data
public class ArticleTagsAndCategory implements Serializable {

    @Id
    private String id;

    //文章 id
    private String articleId;

    //分类 id
    private List<String> categoryId;

    //标签id
    private List<String> tagsId;
}
