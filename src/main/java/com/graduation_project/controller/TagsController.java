package com.graduation_project.controller;

import com.graduation_project.service.impl.CategoryService;
import com.graduation_project.service.impl.TagsService;
import com.graduation_project.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagsController {

  @Autowired
  private TagsService tagsService;

    @GetMapping("/loadall")
    public ResponseResult loadCategoryAndTags(){


        return tagsService.loadTags();
    }
}
