package com.graduation_project.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * 分类和标签表
 */
@Data
public class Category {
    //主键标识，该属性的值会自动对应mongodb的主键字段"_id"，如果该属性名就叫“id”,则该注解可以省略，否则必须写
    @Id
    private String id;//主键

    /**
     * 分类名称
     */
    private String category;

    private List<Tags> tagsList;


}
