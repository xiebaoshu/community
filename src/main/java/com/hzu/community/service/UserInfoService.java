package com.hzu.community.service;


import com.hzu.community.bean.UserInfo;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.UserEnum;
import com.hzu.community.exceptions.UserException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoService {
    public UserEnum save(UserInfo user, ImageHolder imageHolder)
            throws UserException;
    public UserEnum update(UserInfo user, ImageHolder imageHolder)
            throws UserException;
    public int updatePermission(UserInfo user);
    public UserInfo findUserInfoById(@Param("id") Integer id);
    public UserInfo findUserByToken(@Param("token") String token);
//    根据账号，密码，账户类型，寻找
    public List<UserInfo> login(UserInfo user);
    public List<UserInfo> searchList(UserInfo userInfo);
    public Integer searchCount(UserInfo userInfo);
    
}
