package com.hzu.community.service;

import com.hzu.community.bean.SecondArticle;

import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.exceptions.ArticleException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SecondArticleService {
    public ArticleEnum saveArticle(SecondArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleEnum updateArticle(SecondArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleEnum deleteArticle(Integer id, Integer userId)
            throws ArticleException;
    public List<SecondArticle> getArticleList(@Param("articleCondition") SecondArticle articleCondition,
                                              @Param("dateCondition")Integer dateCondition,
                                              @Param("prePrice")Double prePrice,
                                              @Param("nextPrice")Double nextPrice);
    public Integer searchCount(@Param("articleCondition") SecondArticle articleCondition,
                               @Param("dateCondition")Integer dateCondition,
                               @Param("prePrice")Double prePrice,
                               @Param("nextPrice")Double nextPrice);
    public SecondArticle findArticleById(@Param("articleId") Integer articleId);
    void incReadCount(SecondArticle article);
    public void batchDel(@Param( "List" ) List List);
}
