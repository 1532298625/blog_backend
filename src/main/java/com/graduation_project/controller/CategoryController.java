package com.graduation_project.controller;

import com.graduation_project.service.impl.CategoryService;
import com.graduation_project.vo.ResponseResult;
import com.graduation_project.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("category")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

    @GetMapping("/loadall")
    public ResponseResult loadCategoryAndTags(){


        return categoryService.loadCategory();
    }

  @PostMapping("/findArticle")
  public ResponseResult getArticleByCategoryId(@RequestBody PageParams params){

    return categoryService.getArticleByCategoryId(params);
  }
}
