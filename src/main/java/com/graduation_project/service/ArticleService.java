package com.graduation_project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.graduation_project.dao.entity.Article;
import com.graduation_project.vo.ResponseResult;
import com.graduation_project.vo.params.ArticleParams;
import com.graduation_project.vo.params.PageParams;

/**
 * (Article)表服务接口
 *
 * @author makejava
 * @since 2022-05-24 13:34:27
 */
public interface ArticleService extends IService<Article> {

    ResponseResult insertArticle(ArticleParams articleParams);

    ResponseResult updateArticle(ArticleParams articleParams);

    ResponseResult findArticleById(String articleId);
    ResponseResult deleteArticle(String articleId);

    ResponseResult listArticle(PageParams params);

    ResponseResult hotArticle();
    ResponseResult recentArticle();
}

