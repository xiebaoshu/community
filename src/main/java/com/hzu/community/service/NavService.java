package com.hzu.community.service;

import com.hzu.community.bean.HelpArticle;
import com.hzu.community.bean.Nav;
import com.hzu.community.bean.UserInfo;
import com.hzu.community.dto.ArticleExecution;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.NavEnum;
import com.hzu.community.exceptions.ArticleException;
import com.hzu.community.exceptions.NavException;

import java.util.List;

public interface NavService {
    public List<Nav> navDefault();
    public List<Nav> navDiy(UserInfo userInfo);
//    从默认导航栏添加导航
    public int add(Nav nav);
    public void del(Integer id);
    public void delDefault(Integer id);
//    自定义导航增加
    public NavEnum diySave(Nav nav, ImageHolder imageHolder)
            throws NavException;
    public Nav findByName(Nav nav);
    public Nav findDefaultByName(Nav nav);
    public Nav findDefaultById(Nav nav);
//    管理员增加默认导航
    public NavEnum defaultSave(Nav nav, ImageHolder imageHolder)
            throws NavException;
    public NavEnum defaultUpdate(Nav nav, ImageHolder imageHolder) throws NavException;
}
