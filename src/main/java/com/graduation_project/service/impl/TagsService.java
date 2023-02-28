package com.graduation_project.service.impl;

import com.graduation_project.dao.entity.Category;
import com.graduation_project.dao.entity.Tags;
import com.graduation_project.dao.repository.TagsRepository;
import com.graduation_project.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagsService {

    @Autowired
    private TagsRepository tagsRepository;

    public ResponseResult loadTags(){


        List<Tags> tagsList = tagsRepository.findAll();
        return new ResponseResult(200,"标签加载成功",tagsList);
    }
}
