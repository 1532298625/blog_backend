package com.graduation_project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.graduation_project.dao.entity.TimeViewcount;
import com.graduation_project.dao.mapper.TimeViewCountMapper;
import com.graduation_project.service.TimeViewCountService;
import org.springframework.stereotype.Service;

@Service
public class TimeViewCountServiceImpl extends ServiceImpl<TimeViewCountMapper, TimeViewcount> implements TimeViewCountService {
}
