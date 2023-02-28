package com.graduation_project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.graduation_project.dao.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-05-23 18:43:11
 */

public interface SysUserService extends IService<User> {

    Boolean isRegister(String nickName, String email);
}

