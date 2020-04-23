package com.hzu.community.service;

import com.hzu.community.bean.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentService {
    public int addComment(Comment comment);
//    通过评论删除一或二级评论
    public boolean deleteComment(Comment comment) throws RuntimeException;
//    删除文章下所有评论
    public int deleArticleComment(Comment comment);
    //    批量删除文章评论
    public void batchDel(@Param("parId") Integer parId,  @Param( "List" ) List  List);

    //    获取二级回复列表
    public List<Comment> findCommentReplyById(@Param("commentId") Integer commentId);

    //获取一级回复列表(一对多映射二级评论列表)
    public List<Comment> findCommentListById(@Param("articleId") Integer articleId,
                                             @Param("parCategory") Integer parCategory);
    //    通过commentId获取评论
    public Comment findCommentById(@Param("commentId") Long commentId);
}
