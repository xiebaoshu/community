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
    public int add(Nav nav);
    public void del(Integer id);
    public void delDefault(Integer id);
    public NavEnum diySave(Nav nav, ImageHolder imageHolder)
            throws NavException;
    public Nav findByName(Nav nav);
    public Nav findDefaultByName(Nav nav);
    public Nav findDefaultById(Nav nav);
    public NavEnum defaultSave(Nav nav, ImageHolder imageHolder)
            throws NavException;
    public NavEnum defaultUpdate(Nav nav, ImageHolder imageHolder) throws NavException;
}
