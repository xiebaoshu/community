package com.hzu.community.service;

import com.hzu.community.bean.Nav;
import com.hzu.community.bean.UserInfo;

import java.util.List;

public interface NavService {
    public List<Nav> navDefault();
    public List<Nav> navDiy(UserInfo userInfo);
    public int add(Nav nav);
    public void del(Integer id);
}
