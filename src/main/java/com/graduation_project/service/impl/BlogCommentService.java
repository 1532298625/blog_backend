package com.graduation_project.service.impl;

import com.graduation_project.dao.entity.Article;
import com.graduation_project.dao.entity.LoginUser;
import com.graduation_project.dao.entity.User;
import com.graduation_project.dao.repository.CommentRepository;
import com.graduation_project.dao.entity.Comment;
import com.graduation_project.service.ArticleService;
import com.graduation_project.service.SysUserService;
import com.graduation_project.utils.BeanCopyUtils;
import com.graduation_project.vo.ResponseResult;
import com.graduation_project.vo.UserVo;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogCommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SysUserService userService;
    @Autowired
    private ArticleService articleService;

    /**
     * 查询所有评论
     * @param articleId
     * @return
     */
    public ResponseResult findAllCommentByArticleId(String articleId) {
        List<Comment> commentList = commentRepository.findCommentByArticleid(articleId);
        //顶级评论 楼主评论
        List<Comment> louhzus =
                commentList.stream().filter(comment -> comment.getParentid().equals("0")).collect(Collectors.toList());
        //这里抽出了一个查找子评论的方法
        List comments = getSubCommentList(commentList, louhzus);

        return new ResponseResult(200,comments);

    }

    /**
     * 新增评论
     * @param
     * @return
     */
    public ResponseResult addComment(Comment comment){
        if (!StringUtils.hasText(comment.getContent())) {
            return new ResponseResult(500,"评论内容不能为空");
        }
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String articleid = comment.getArticleid();
        Article article = articleService.getById(articleid);
        article.setCommentCounts(article.getCommentCounts()+1);
        articleService.updateById(article);

        String nickName = loginUser.getUser().getNickName();
        Long userId = loginUser.getUser().getId();
        comment.setState("0");
        comment.setCreatedatetime(LocalDateTime.now());
        comment.setLikenum(0);
        comment.setReplynum(0);
        comment.setUserid(String.valueOf(userId));
        comment.setNickname(nickName);
        //维护一下 评论数
        if (!comment.getParentid().equals("0")){
            updateById(comment.getParentid(),true);
        }
        commentRepository.insert(comment);
        return new ResponseResult(200,"评论成功");
    }

    /**
     *    删除评论
     * @param
     * @return
     */
    @Transactional
    public ResponseResult delComment(String commentId){
        //还要删除 父评论的评论数
        Comment comment = commentRepository.findById(commentId).get();
        Article article = articleService.getById(comment.getArticleid());
        article.setCommentCounts(article.getCommentCounts()-1);
        articleService.updateById(article);
        // TODO 获取用户信息
        if (comment.getUserid().equals("")) {
            return new ResponseResult<>(500,"评论不存在");
        }
        updateById(comment.getParentid(),false);
        //删除该评论
        commentRepository.deleteById(commentId);
        return new ResponseResult<>(200,"评论已删除");
    }

    /**
     * 点赞操作
     * @param commentId
     * @param userId
     * @return
     */
    public ResponseResult likeComment(String commentId,String userId) {
        Comment comment = commentRepository.findById(commentId).get();
        List<String> whoLikeThisComment = new ArrayList<>();
        if (!CollectionUtils.isEmpty(comment.getWhoLikeThisComment())) {
            //如果有人评论  就把 对应的用户id拿出来做判断
            whoLikeThisComment = comment.getWhoLikeThisComment();
            if (whoLikeThisComment.contains(userId)) {
                whoLikeThisComment.remove(userId);
                comment.setWhoLikeThisComment(whoLikeThisComment);
                //维护一下点赞数
                comment.setLikenum(comment.getLikenum()-1);
                commentRepository.save(comment);
                return new ResponseResult<>(200,"取消点赞成功");
            }
        }
        whoLikeThisComment.add(userId);
        comment.setWhoLikeThisComment(whoLikeThisComment);
        //维护一下点赞数
        comment.setLikenum(comment.getLikenum()+1);
        commentRepository.save(comment);
        return new ResponseResult<>(200,"点赞成功");
    }

    //    取消点赞
    public ResponseResult cancelLike(String commentId,String userId) {
      /*  Comment comment = commentRepository.findById(commentId).get();
        List<String> whoLikeThisComment = new ArrayList<>();
        if (!CollectionUtils.isEmpty(comment.getWhoLikeThisComment())) {
            //如果有人评论  就把 对应的用户id拿出来做判断
            whoLikeThisComment = comment.getWhoLikeThisComment();
            if (!whoLikeThisComment.contains(userId)) {
                return new ResponseResult<>(200,null);
            }
        }
        whoLikeThisComment.remove(userId);
        comment.setWhoLikeThisComment(whoLikeThisComment);
        //维护一下点赞数
        comment.setLikenum(comment.getLikenum()-1);
        commentRepository.save(comment);*/
        return new ResponseResult<>(200,"取消点赞成功");
    }
    public long updateById(String id,boolean isInc) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update();
        if (isInc) {
            update.inc("replynum",1);
        }else {
            update.inc("replynum",-1);
        }
        val matchedCount = mongoTemplate.updateFirst(query, update, Comment.class).getMatchedCount();
        return matchedCount;
    }



    private List<Comment> getSubCommentList(List<Comment> commentList, List<Comment> louhzus) {
        //一级子评论
        for (Comment louzhu : louhzus) {
            if (louzhu.getReplynum() == 0){
                return louhzus;
            }
            List<Comment> firstClassComments = new ArrayList<>();
            for (Comment subComment : commentList) {
                if (subComment.getParentid().equals(louzhu.getId())) {
                    //把给谁评论封装一下
                    User user = userService.getById(subComment.getUserid());
                    UserVo userVo = BeanCopyUtils.copyBean(user, UserVo.class);
                    subComment.setToUser(userVo);
                    firstClassComments.add(subComment);
                    //递归 找到 二级 或者 多级子评论
                    getSubCommentList(commentList,Arrays.asList(subComment));
                }
            }
            louzhu.setSubComment(firstClassComments);
        }
        return louhzus;
    }
}
