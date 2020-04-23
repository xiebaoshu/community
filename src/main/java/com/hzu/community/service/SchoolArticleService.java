package com.hzu.community.service;

import com.hzu.community.bean.SchoolArticle;

import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.exceptions.ArticleException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SchoolArticleService {
    public ArticleEnum saveArticle(SchoolArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleEnum updateArticle(SchoolArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleEnum deleteArticle(Integer id, Integer userId)
            throws ArticleException;

    public List<SchoolArticle> getArticleList(@Param("articleCondition") SchoolArticle articleCondition,
                                              @Param("dateCondition")Integer dateCondition);
    public Integer searchCount(@Param("articleCondition") SchoolArticle articleCondition,
                               @Param("dateCondition")Integer dateCondition);
    public SchoolArticle findArticleById(@Param("articleId") Integer articleId);

    public void incReadCount(SchoolArticle article);
    public void batchDel(@Param( "List" ) List List);
}
