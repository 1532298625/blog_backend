package com.graduation_project.dao.repository;

import com.graduation_project.dao.entity.Tags;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TagsRepository extends MongoRepository<Tags,String> {
    List<Tags> findByCategoryId(String categoryId);
}
