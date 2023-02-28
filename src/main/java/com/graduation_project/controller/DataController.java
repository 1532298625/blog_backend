package com.graduation_project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.graduation_project.dao.entity.*;
import com.graduation_project.dao.mapper.SysUserMapper;
import com.graduation_project.dao.repository.ArticleTagsAndCategoryRepository;
import com.graduation_project.dao.repository.CategoryRepository;
import com.graduation_project.dao.repository.TagsRepository;
import com.graduation_project.service.SysUserService;
import com.graduation_project.service.TimeViewCountService;
import com.graduation_project.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired
    private ArticleTagsAndCategoryRepository articleTagsAndCategory;
    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysUserMapper userMapper;

    @GetMapping("/sex")
    @PreAuthorize("hasAuthority('system:admin:getdata')")
    public ResponseResult getSexData(){
        List<Map<String, Integer>> sexData = userMapper.getSexData();
        return new ResponseResult(200,sexData);
    }

    @GetMapping("/article")
    @PreAuthorize("hasAuthority('system:admin:getdata')")
    public ResponseResult getArticle(){
        List<Map<String, Object>> articleData = userMapper.getArticleData();
        return new ResponseResult(200,articleData);
    }

    @GetMapping("/category")
    @PreAuthorize("hasAuthority('system:admin:getdata')")
    public ResponseResult getCategory(){
        List<Map<String, Object>> articleData = userMapper.getAllArticleData();
        HashMap<String,Integer> wordMap = new HashMap<>();
        for (int i = 0; i < articleData.size(); i++) {
            String articleId = articleData.get(i).get("id").toString();
            ArticleTagsAndCategory categoryAndTags = articleTagsAndCategory.findByArticleId(articleId);
            List<String> categoryId = categoryAndTags.getCategoryId();
            Iterable<Category> allCategory = categoryRepository.findAllById(categoryId);
            ArrayList<Category> categories = Lists.newArrayList(allCategory);
            for (int j = 0; j < categories.size(); j++) {
                if(wordMap.containsKey(categories.get(j).getCategory())){
                    Integer value = wordMap.get(categories.get(j).getCategory());
                    wordMap.put(categories.get(j).getCategory(), value+Integer.valueOf(articleData.get(i).get("view_counts").toString()));
                }else{
                    wordMap.put(categories.get(j).getCategory(), Integer.valueOf(articleData.get(i).get("view_counts").toString()));
                }
            }
        }

        return new ResponseResult(200,wordMap);
    }

    @GetMapping("/tags")
    @PreAuthorize("hasAuthority('system:admin:getdata')")
    public ResponseResult getTags(){
        List<Map<String, Object>> articleData = userMapper.getAllArticleData();
        HashMap<String,Integer> wordMap = new HashMap<>();
        for (int i = 0; i < articleData.size(); i++) {
            String articleId = articleData.get(i).get("id").toString();
            ArticleTagsAndCategory categoryAndTags = articleTagsAndCategory.findByArticleId(articleId);
            List<String> tagsId = categoryAndTags.getTagsId();
            Iterable<Tags> tags = tagsRepository.findAllById(tagsId);
            ArrayList<Tags> alltags = Lists.newArrayList(tags);
            for (int j = 0; j < alltags.size(); j++) {
                if(wordMap.containsKey(alltags.get(j).getTags())){
                    Integer value = wordMap.get(alltags.get(j).getTags());
                    wordMap.put(alltags.get(j).getTags(), value+Integer.valueOf(articleData.get(i).get("view_counts").toString()));
                }else{
                    wordMap.put(alltags.get(j).getTags(), Integer.valueOf(articleData.get(i).get("view_counts").toString()));
                }
            }
        }

        return new ResponseResult(200,wordMap);
    }

    @Autowired
    private TimeViewCountService timeViewCountService;
    @GetMapping("/timeData")
    @PreAuthorize("hasAuthority('system:admin:getdata')")
    public ResponseResult getTimeData(){
        QueryWrapper<TimeViewcount> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("time");
        queryWrapper.last("limit 15");
        List<TimeViewcount> list = timeViewCountService.list(queryWrapper);
        return new ResponseResult(200,list);
    }
}
