package com.graduation_project.dao.entity;

import com.graduation_project.vo.UserVo;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Comment implements Serializable {
    //主键标识，该属性的值会自动对应mongodb的主键字段"_id"，如果该属性名就叫“id”,则该注解可以省略，否则必须写
    @Id
    private String id;//主键
    private String articleid;
    //该属性对应mongodb的字段的名字，如果一致，则无需该注解
//    @Field("content")
    private String content;//吐槽内容
 /*   private Date publishtime;//发布日期*/
    //添加了一个单字段的索引
    private String userid;//发布人ID
    private String nickname;//昵称
    private LocalDateTime createdatetime;//评论的日期时间
    private Integer likenum;//点赞数
    private Integer replynum;//回复数
    private String state;//状态
    private UserVo toUser;
    private String parentid;//上级ID

    //给评论点赞的 用户id集合；
    private List<String> whoLikeThisComment;
    //子评论
  private   List<Comment> subComment;

}
