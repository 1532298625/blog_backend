package com.graduation_project.dao.repository;

import com.graduation_project.dao.entity.ArticleTagsAndCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ArticleTagsAndCategoryRepository extends MongoRepository<ArticleTagsAndCategory,String> {

    ArticleTagsAndCategory findByArticleId(String articleId);

    Long deleteByArticleId(String articleId);

    @Query(value = "{'categoryId':?0}")
    List<ArticleTagsAndCategory> findAllArticlesByCategoryId(String categoryId);

    @Query(value = "{'tagsId':?0}")
    List<ArticleTagsAndCategory> findAllArticlesByTagId(String tagId);
}
