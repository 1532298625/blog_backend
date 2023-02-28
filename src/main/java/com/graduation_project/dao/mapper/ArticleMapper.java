package com.graduation_project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.graduation_project.dao.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * (Article)表数据库访问层
 *
 * @author makejava
 * @since 2022-05-24 13:34:18
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    int getArticleCount();
}

