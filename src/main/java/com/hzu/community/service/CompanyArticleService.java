package com.hzu.community.service;

import com.hzu.community.bean.CompanyArticle;
import com.hzu.community.dto.ArticleExecution;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.exceptions.ArticleException;

public interface CompanyArticleService {
    public ArticleExecution saveArticle(CompanyArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleExecution updateArticle(CompanyArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleExecution deleteArticle(Integer id, Integer userId)
            throws ArticleException;
}
