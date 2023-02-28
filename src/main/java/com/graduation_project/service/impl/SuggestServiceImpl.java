package com.graduation_project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.graduation_project.dao.entity.Suggest;
import com.graduation_project.dao.mapper.SuggestMapper;
import com.graduation_project.service.SuggestService;
import org.springframework.stereotype.Service;

@Service
public class SuggestServiceImpl extends ServiceImpl<SuggestMapper, Suggest> implements SuggestService {
    @Override
    public Page<Suggest> list(String username, String title, String content, Integer pageSize, Integer pageNum) {
        Page<Suggest> page = new Page<>(pageNum,pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        if (StrUtil.isNotEmpty(username)){
            wrapper.like("username", username);
        }
        if (StrUtil.isNotEmpty(content)){
            wrapper.like("content", content);
        }
        if (StrUtil.isNotEmpty(title)){
            wrapper.like("title", title);
        }
        return page(page,wrapper);
    }
}
