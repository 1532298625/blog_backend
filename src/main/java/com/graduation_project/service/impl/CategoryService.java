package com.graduation_project.service.impl;

import com.graduation_project.dao.entity.Article;
import com.graduation_project.dao.entity.ArticleTagsAndCategory;
import com.graduation_project.dao.repository.ArticleTagsAndCategoryRepository;
import com.graduation_project.dao.repository.CategoryRepository;
import com.graduation_project.dao.repository.TagsRepository;
import com.graduation_project.dao.entity.Category;
import com.graduation_project.service.ArticleService;
import com.graduation_project.vo.ResponseResult;
import com.graduation_project.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private ArticleTagsAndCategoryRepository articleTagsAndCategoryRepository;
    @Autowired
    private ArticleService articleService;

    public ResponseResult loadCategory(){


        List<Category> categoryList = categoryRepository.findAll();

        return new ResponseResult(200,"分类加载成功",categoryList);
    }

    public ResponseResult getArticleByCategoryId(PageParams params) {
        List<ArticleTagsAndCategory> all = articleTagsAndCategoryRepository.findAll();
        List<ArticleTagsAndCategory> collect =
                all.stream().filter(article -> article.getCategoryId().equals(params.getCategoryId())).collect(Collectors.toList());
        List<String> articleId = collect.stream().map(ArticleTagsAndCategory::getArticleId).collect(Collectors.toList());
        List<Article> articles = articleService.listByIds(articleId);
        return ResponseResult.success(articles);
    }

    public List<ArticleTagsAndCategory> getArticleByCategoryId2(PageParams params) {
        List<ArticleTagsAndCategory> all = articleTagsAndCategoryRepository.findAllArticlesByCategoryId(params.getCategoryId());
        return all;
    }

    public List<ArticleTagsAndCategory> getArticleByTagId(PageParams params) {
        List<ArticleTagsAndCategory> all = articleTagsAndCategoryRepository.findAllArticlesByTagId(params.getTagId());
        return all;
    }
}
