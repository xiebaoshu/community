package com.hzu.community.service;

import com.hzu.community.bean.HelpArticle;
import com.hzu.community.bean.LostArticle;
import com.hzu.community.dto.ArticleExecution;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.exceptions.ArticleException;

public interface HelpArticleService {
    public ArticleExecution saveArticle(HelpArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleExecution updateArticle(HelpArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleExecution deleteArticle(Integer id, Integer userId)
            throws ArticleException;
}