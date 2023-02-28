package com.graduation_project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.graduation_project.dao.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author Dobby
 * @create 2022/7/24 20:27
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(long userid);
}
