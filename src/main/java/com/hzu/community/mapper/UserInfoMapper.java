package com.hzu.community.mapper;


import com.hzu.community.bean.UserInfo;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserInfoMapper {
    @Select("select * from user_info where user_id=#{id}")
    public UserInfo findUserInfoById(@Param("id") Integer id);

    @Select("select * from user_info where token=#{token}")
    public UserInfo findUserByToken(@Param("token") String token);

    @Select("<script> " +
            "select * from user_info " +
            " <where> " +
            " <if test=\"userAccount != null\">and user_account = #{userAccount}</if> " +
            " <if test=\"userPassword != null\">and user_password =#{userPassword}</if> " +
            " <if test=\"userType != null\">and user_type =#{userType}</if> " +
            " </where> " +
            " </script> ")
    public List<UserInfo> login(UserInfo user);

    @Options(useGeneratedKeys = true,keyProperty = "userId")
    @Insert("insert into user_info(user_account,user_password," +
            "user_name,gender,user_class," +
            "user_type,description,user_img) " +
            "values(#{userAccount}," +
            "#{userPassword}," +
            "#{userName}," +
            "#{gender}," +
            "#{userClass}," +
            "#{userType}," +
            "#{description}," +
            "#{userImg}" +

            ")")
    public int register(UserInfo user);

    @Update("<script> " +
            "update  user_info " +
            "<set>" +
            "<if test=\"userAccount != null\"> user_account = #{userAccount},</if>" +
            "<if test=\"userPassword != null\"> user_password = #{userPassword},</if>" +
            "<if test=\"userName != null\"> user_name = #{userName},</if>" +
            "<if test=\"gender != null\"> gender = #{gender},</if>" +
            "<if test=\"userClass != null\"> user_class = #{userClass},</if>" +
            "<if test=\"userImg != null\"> user_img = #{userImg},</if>" +
            "<if test=\"userType != null\"> user_type = #{userType},</if>" +
            "<if test=\"description != null\"> description = #{description},</if>" +
            "<if test=\"navType != null\"> nav_type = #{navType},</if>" +
            "<if test=\"token != null\"> token = #{token},</if>" +
            "<if test=\"permission != null\"> permission = #{permission},</if>" +
            "<if test=\"navType != null\"> nav_type = #{navType}</if>" +
            "</set>" +
            "where user_id = #{userId}" +
            " </script> ")
    public int update(UserInfo user);

    @Select("<script> " +
            "select * from user_info " +
            " <where> " +
            " <if test=\"userName != null\">and user_name LIKE '%${userName}%'</if> " +
            " <if test=\"userType != null\">and user_type = #{userType}</if> " +
            " <if test=\"permission != null\">and permission = #{permission}</if> " +
            " </where> " +
            " </script> ")
    public List<UserInfo> searchList(UserInfo userInfo);

    @Select("<script> " +
            "select count(1) from user_info " +
            " <where> " +
            " <if test=\"userName != null\">and user_name LIKE '%${userName}%'</if> " +
            " <if test=\"userType != null\">and user_type = #{userType}</if> " +
            " <if test=\"permission != null\">and permission = #{permission}</if> " +
            " </where> " +
            " </script> ")
    public Integer searchCount(UserInfo userInfo);


}
