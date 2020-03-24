package com.hzu.community.mapper;

import com.hzu.community.bean.Notification;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface NotificationMapper {
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into notification" +
            "(notifier_id,receiver_id,article_id,article_par_category," +
            "type,create_time,outerTitle,status)" +
            "values(#{notifier.userId},#{receiver.userId},#{articleId},#{articleParCategory}," +
            "#{type},#{createTime},#{outerTitle},#{status})")
    public int addNotification(Notification notification);

    //    信息通知，以未读排序
    @Select("select * from notification where receiver_id = #{userId} and notifier_id != #{userId} order by status asc")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="notifier_id",property="notifier",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="receiver_id",property="receiver",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="article_id",property="articleId"),
            @Result(column="article_par_category",property="articleParCategory"),
            @Result(column="type",property="type"),
            @Result(column="create_time",property="createTime"),
            @Result(column="outerTitle",property="outerTitle"),
            @Result(column="status",property="status")

    })
    public List<Notification> notificationList(@Param("userId") Integer userId);


//    他人主页：他的回复或者他发表的评论
    @Select("select * from notification where notifier_id = #{userId} order by create_time desc")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="notifier_id",property="notifier",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="receiver_id",property="receiver",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="article_id",property="articleId"),
            @Result(column="article_par_category",property="articleParCategory"),
            @Result(column="type",property="type"),
            @Result(column="create_time",property="createTime"),
            @Result(column="outerTitle",property="outerTitle"),


    })
    public List<Notification> replyList(@Param("userId") Integer userId);

    @Select("select * from notification where id = #{id}")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="notifier_id",property="notifier",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="receiver_id",property="receiver",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="article_id",property="articleId"),
            @Result(column="article_par_category",property="articleParCategory"),
            @Result(column="type",property="type"),
            @Result(column="create_time",property="createTime"),
            @Result(column="outerTitle",property="outerTitle"),
            @Result(column="status",property="status")

    })
    public Notification getNotificationById(@Param("id") Long id);

    @Update("<script> " +
            "update notification " +
            " <set> " +
            " <if test=\"status != null\"> status = #{status}</if> " +

            " </set> " +
            "where id = #{id}" +
            " </script> ")
    public int updateNotification(Notification notification);

    @Select("select count(1) from notification where status = 0 and receiver_id = #{userId} and notifier_id != #{userId}")
    public Long countUnread(@Param("userId") Integer userId);

    @Delete("delete from notification where article_id = #{articleId} and article_par_category = #{articleParCategory}")
    public int delNotification(Notification notification);


}
