package com.hzu.community.service;

import com.hzu.community.bean.SchoolArticle;
import com.hzu.community.dto.ArticleExecution;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.exceptions.ArticleException;

public interface SchoolArticleService {
    public ArticleExecution saveArticle(SchoolArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleExecution updateArticle(SchoolArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleExecution deleteArticle(Integer id, Integer userId)
            throws ArticleException;
}
