package com.hzu.community.service;


import com.hzu.community.bean.UserInfo;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.UserEnum;
import com.hzu.community.exceptions.UserException;

import java.util.List;

public interface UserInfoService {
    public UserEnum save(UserInfo user, ImageHolder imageHolder)
            throws UserException;
    public UserEnum update(UserInfo user, ImageHolder imageHolder)
            throws UserException;
    public int updatePermission(UserInfo user);
    public List<UserInfo> searchList(UserInfo userInfo);
    public Integer searchCount(UserInfo userInfo);
    
}
