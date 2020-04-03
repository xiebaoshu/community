package com.hzu.community.service;

import com.hzu.community.bean.LostArticle;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.dto.ArticleExecution;
import com.hzu.community.exceptions.ArticleException;

public interface LostArticleService {
   public ArticleExecution saveArticle(LostArticle lostArticle, ImageHolder imageHolder) throws ArticleException;
   public ArticleExecution updateArticle(LostArticle lostArticle, ImageHolder imageHolder) throws ArticleException;
   public ArticleExecution deleteArticle(Integer id, Integer userId) throws ArticleException;
}
