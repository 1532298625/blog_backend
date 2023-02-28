package com.graduation_project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.graduation_project.dao.entity.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-05-23 18:43:09
 */
@Mapper
public interface SysUserMapper extends BaseMapper<User> {

    User isUserRegister(@Param("nickName") String nickName, @Param("email") String email);

    @MapKey("sex")
    List<Map<String,Integer>> getSexData();

    @MapKey("id")
    List<Map<String,Object>> getArticleData();

    @MapKey("id")
    List<Map<String,Object>> getAllArticleData();
}

