package com.hzu.community.service;

import com.hzu.community.bean.JobArticle;
import com.hzu.community.dto.ArticleExecution;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.exceptions.ArticleException;

public interface JobArticleService {
    public ArticleExecution saveArticle(JobArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleExecution updateArticle(JobArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleExecution deleteArticle(Integer id, Integer userId)
            throws ArticleException;
}
