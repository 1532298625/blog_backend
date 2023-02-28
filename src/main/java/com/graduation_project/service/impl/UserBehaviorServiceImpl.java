package com.graduation_project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.graduation_project.dao.entity.UserBehavior;
import com.graduation_project.dao.mapper.UserBehaviorMapper;

import com.graduation_project.service.UserBehaviorService;
import org.springframework.stereotype.Service;

@Service
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior> implements UserBehaviorService {
}
