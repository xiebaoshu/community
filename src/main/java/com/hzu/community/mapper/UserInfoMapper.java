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

    @Update("update  user_info set token = #{token} where user_id = #{userId}")
    public int update(UserInfo user);
}
