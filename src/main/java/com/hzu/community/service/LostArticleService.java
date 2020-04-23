package com.hzu.community.service;

import com.hzu.community.bean.LostArticle;
import com.hzu.community.dto.ImageHolder;

import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.exceptions.ArticleException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LostArticleService {
   public ArticleEnum saveArticle(LostArticle lostArticle, ImageHolder imageHolder) throws ArticleException;
   public ArticleEnum updateArticle(LostArticle lostArticle, ImageHolder imageHolder) throws ArticleException;
   public ArticleEnum deleteArticle(Integer id, Integer userId) throws ArticleException;

   public List<LostArticle> getArticleList(@Param("articleCondition") LostArticle articleCondition,
                                           @Param("dateCondition")Integer dateCondition);
   public Integer searchCount(@Param("articleCondition") LostArticle articleCondition,
                              @Param("dateCondition")Integer dateCondition);
   public LostArticle findArticleById(@Param("articleId") Integer articleId);
   public void incReadCount(LostArticle lostArticle);
   public void batchDel(@Param( "List" ) List List);
}
