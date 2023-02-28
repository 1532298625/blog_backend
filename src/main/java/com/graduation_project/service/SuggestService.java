package com.graduation_project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.graduation_project.dao.entity.Suggest;

public interface SuggestService extends IService<Suggest> {
    Page<Suggest> list(String username, String title, String content, Integer pageSize, Integer pageNum);
}
