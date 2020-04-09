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

    @Select("select * from user_info where user_account=#{username} and user_password=#{password}")
    public List<UserInfo> sign(@Param("username") String username,@Param("password") String password);

    @Update("<script> " +
            "update  user_info " +
            "<set>" +
            "<if test=\"token != null\"> token = #{token},</if>" +
            "<if test=\"navType != null\"> nav_type = #{navType}</if>" +
            "</set>" +
            "where user_id = #{userId}" +
            " </script> ")
    public int update(UserInfo user);

    @Select("select * from user_info where user_name LIKE '%${search}%'")
    public List<UserInfo> searchList(@Param("search") String search);

    @Select("select count(1) from user_info where user_name LIKE '%${search}%'")
    public Integer searchCount(@Param("search") String search);


}
