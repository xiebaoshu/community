package com.hzu.community.service;


import com.hzu.community.bean.UserInfo;
import com.hzu.community.dto.UserExecution;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.exceptions.UserException;

import java.util.List;

public interface UserInfoService {
    public UserExecution save(UserInfo user, ImageHolder imageHolder)
            throws UserException;
    public UserExecution update(UserInfo user, ImageHolder imageHolder)
            throws UserException;
    public int updatePermission(UserInfo user);
    public List<UserInfo> searchList(UserInfo userInfo);
    public Integer searchCount(UserInfo userInfo);
    
}
