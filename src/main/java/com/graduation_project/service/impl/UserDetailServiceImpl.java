package com.graduation_project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;


import com.graduation_project.dao.entity.LoginUser;
import com.graduation_project.dao.entity.User;
import com.graduation_project.dao.mapper.MenuMapper;
import com.graduation_project.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService userService;
    @Autowired
    private MenuMapper menuMapper;
    @Override                              //用户传递过来的邮箱
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //mybatis-plus为我们提供的条件构建器
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        User user = userService.getOne(queryWrapper);
        if(Objects.isNull(user)){
            throw new RuntimeException("邮箱或密码错误");
        }
        //封装成UserDetails对象返回
        //TODO 根据用户查询权限信息 添加到LoginUser中
        List<String> list = menuMapper.selectPermsByUserId(user.getId());
        return new LoginUser(user,list);

    }
}
