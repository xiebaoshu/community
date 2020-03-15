package com.hzu.community.mapper;


import com.hzu.community.bean.UserInfo;

import org.apache.ibatis.annotations.*;

@Mapper
public interface UserInfoMapper {
    @Select("select * from user_info where user_id=#{id}")
    public UserInfo findUserInfoById(@Param("id") int id);
}
