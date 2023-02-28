package com.graduation_project.controller;


import com.graduation_project.common.aop.LogAnnotation;
import com.graduation_project.dao.mapper.ArticleMapper;
import com.graduation_project.service.ArticleService;
import com.graduation_project.vo.ResponseResult;
import com.graduation_project.vo.params.ArticleParams;
import com.graduation_project.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * (Article)表控制层
 *
 * @author makejava
 * @since 2022-05-24 13:34:18
 */
@RestController
@RequestMapping("article")
public class ArticleController  {

    @Autowired
    private ArticleService articleService;

    /**
     * 新增
     * @param articleParams
     * @return
     */
    @PostMapping("/post")
    @PreAuthorize("hasAuthority('system:admin:addArticle')")
    public ResponseResult insertArticle(@RequestBody  ArticleParams articleParams) {

        return articleService.insertArticle(articleParams);
    }

    /**
     * 编辑
     * @param articleParams
     * @return
     */
    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('system:admin:editArticle')")
    public ResponseResult editArticle(@RequestBody  ArticleParams articleParams) {

        return articleService.updateArticle(articleParams);
    }

    /**
     *  根据 id 查找 文章
     * @param articleId
     * @return
     */
    @GetMapping ("/{id}")
    @LogAnnotation(module = "文章",operation = "查看文章详情")
    public ResponseResult findtArticle(@PathVariable("id") String articleId) {
        return articleService.findArticleById(articleId);
    }
    @PostMapping ("/list")
    @LogAnnotation(module = "文章",operation = "批量查询文章")
    public ResponseResult listArticle(@RequestBody PageParams pageParams) {

        return articleService.listArticle(pageParams);
    }

    /**
     *  根据 id 删除
     * @param articleId
     * @return
     */
    @GetMapping ("/delete/{id}")
    @PreAuthorize("hasAuthority('system:admin:deleteArticle')")
    public ResponseResult deleteArticle(@PathVariable("id") String articleId) {

        return articleService.deleteArticle(articleId);
    }
    @GetMapping("/hotArticle")
    public ResponseResult hotArticle(){
        return articleService.hotArticle();
    }

    @GetMapping("/recentArticle")
    public ResponseResult recentArticle(){
        return articleService.recentArticle();
    }

}

