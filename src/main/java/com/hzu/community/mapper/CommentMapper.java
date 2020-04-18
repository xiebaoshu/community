package com.hzu.community.mapper;

import com.hzu.community.bean.Comment;

import jdk.nashorn.internal.objects.annotations.Where;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

//    获取二级回复列表
    @Select("select * from comment where parent_Id = #{commentId}")
    @Results({
            @Result(id=true,column="comment_id",property="commentId"),
            @Result(column="article_id",property="articleId"),
            @Result(column="user_id",property="user",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="content",property="content"),
            @Result(column="is_admin",property="isAdmin"),
            @Result(column="parent_id",property="parentComment",one = @One(select = "com.hzu.community.mapper.CommentMapper.findCommentById")),
            @Result(column="reply_user_id",property="replyUser",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="reply_is_admin",property="replyIsAdmin")

    })
    public List<Comment> findCommentReplyById(@Param("commentId") Integer commentId);


    //获取一级回复列表
    @Select("<script> " +
            "SELECT * " +
            "from comment " +
            " <where> " +
            " <if test=\"articleId != null\">and article_id = #{articleId}</if> " +
            " <if test=\"parCategory != null\">and article_par_category = #{parCategory}</if> " +
            "and parent_id is null"+
            " </where> "+
            " </script> ")
    @Results({
            @Result(id=true,column="comment_id",property="commentId"),
            @Result(column="article_id",property="articleId"),
            @Result(column="user_id",property="user",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="content",property="content"),
            @Result(column="comment_id",property="commentList",many = @Many(select = "com.hzu.community.mapper.CommentMapper.findCommentReplyById")),
            @Result(column="is_admin",property="isAdmin"),
            @Result(column="create_time",property="createTime")


    })
    public List<Comment> findCommentListById(@Param("articleId") Integer articleId,
                                             @Param("parCategory") Integer parCategory);


//    通过commentId获取评论
    @Select("select * from comment where comment_id = #{commentId}")
    @Results({
            @Result(id=true,column="comment_id",property="commentId"),
            @Result(column="article_id",property="articleId"),
            @Result(column="user_id",property="user",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="content",property="content"),
            @Result(column="is_admin",property="isAdmin"),
            @Result(column="create_time",property="createTime")

    })
    public Comment findCommentById(@Param("commentId") Long commentId);




    @Options(useGeneratedKeys = true,keyProperty = "commentId")
    @Insert("insert into comment(article_id,user_id,content,parent_id," +
            "is_admin,create_time,reply_user_id,reply_is_admin,article_par_category)"+
            "values(#{articleId}," +
            "#{user.userId}," +
            "#{content}," +
            "#{parentComment.commentId}," +
            "#{isAdmin}," +
            "#{createTime}," +
            "#{replyUser.userId}," +
            "#{replyIsAdmin},"+
            "#{articleParCategory}"+
            ")")
    public int addComment(Comment comment);

    @Delete("delete from comment where comment_id = #{commentId}")
    public int deleComment(Comment comment);

//    删除一级评论下的二级评论
    @Delete("delete from comment where parent_id = #{commentId}")
    public int deleCommentList(Comment comment);
//    删除文章评论
    @Delete("delete from comment where article_id = #{articleId} and article_par_category = #{articleParCategory}")
    public int deleArticleComment(Comment comment);
//    批量删除文章评论
    @Delete( " <script>" +
            "delete from comment where article_par_category = #{parId} and article_id in" +
            " <foreach collection='List' open='(' item='article' separator=',' close=')'> #{article.id}</foreach> "+
            " </script>" )
    public void batchDel(@Param("parId") Integer parId,  @Param( "List" ) List  List);
}
