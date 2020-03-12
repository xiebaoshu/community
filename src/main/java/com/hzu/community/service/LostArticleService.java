package com.hzu.community.service;

import com.hzu.community.bean.LostArticle;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.dto.LostArticleExecution;
import com.hzu.community.exceptions.LostArticleException;

public interface LostArticleService {
   public LostArticleExecution saveArticle(LostArticle lostArticle, ImageHolder imageHolder) throws LostArticleException;
}
