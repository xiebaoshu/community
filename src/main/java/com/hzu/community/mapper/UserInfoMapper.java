package com.hzu.community.mapper;


import com.hzu.community.bean.UserInfo;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserInfoMapper {
    @Select("select * from user_info where user_id=#{id}")
    public UserInfo findUserInfoById(@Param("id") Integer id);

    @Select("select * from user_info where user_account=#{username} and user_password=#{password}")
    public List<UserInfo> sign(@Param("username") String username,@Param("password") String password);
}
