package com.graduation_project.controller;


import com.graduation_project.dao.entity.Comment;
import com.graduation_project.vo.ResponseResult;
import com.graduation_project.service.impl.BlogCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comment")
public class CommentController {


    @Autowired
    private BlogCommentService blogCommentService;

    /**
     * 查询所有评论
     * @param articleId
     * @return
     */
    @GetMapping("/listAll/{articleId}")
    public ResponseResult listAll(@PathVariable("articleId") String articleId) {
        return blogCommentService.findAllCommentByArticleId(articleId);
    }

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @PostMapping("/addComment")
    @PreAuthorize("hasAuthority('system:common:addComment')")
    public ResponseResult addComment(@RequestBody Comment comment) {
        return blogCommentService.addComment(comment);
    }

    /**
     * 删除评论
     * @param commentId
     * @return
     */
    @GetMapping("/delComment/{commentId}")
    @PreAuthorize("hasAuthority('system:common:deleteComment')")
    public ResponseResult delComment(@PathVariable("commentId") String commentId) {
        return blogCommentService.delComment(commentId);
    }

    /**
     * 给评论点赞
     * @param commentId
     * @return
     */
    @GetMapping("/likeComment/{commentId}")
    public ResponseResult likeComment(@PathVariable("commentId") String commentId) {
        return blogCommentService.likeComment(commentId,"u1");
    }

}
