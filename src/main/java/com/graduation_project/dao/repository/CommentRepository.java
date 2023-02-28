package com.graduation_project.dao.repository;


import com.graduation_project.dao.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment,String> {
    List<Comment> findCommentByArticleid(String articleId);
    List<Comment> findCommentByArticleid(Double articleId);

    List<Comment> findCommentByParentid(String parentId);

}
