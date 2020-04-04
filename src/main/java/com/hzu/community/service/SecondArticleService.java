package com.hzu.community.service;

import com.hzu.community.bean.SecondArticle;
import com.hzu.community.dto.ArticleExecution;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.exceptions.ArticleException;

public interface SecondArticleService {
    public ArticleExecution saveArticle(SecondArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleExecution updateArticle(SecondArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleExecution deleteArticle(Integer id, Integer userId)
            throws ArticleException;
}
