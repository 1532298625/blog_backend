package com.graduation_project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.graduation_project.dao.mapper.SysUserMapper;
import com.graduation_project.dao.entity.User;
import com.graduation_project.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-05-23 18:43:12
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, User>implements SysUserService {

    @Override
    public Boolean isRegister(String nickName, String email) {
      User user  = baseMapper.isUserRegister(nickName,email);
        if (Objects.isNull(user)) {
            return false;
        }
        return true;
    }
}

