package com.hzu.community.service;

import com.hzu.community.bean.JobArticle;

import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.exceptions.ArticleException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobArticleService {
    public ArticleEnum saveArticle(JobArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleEnum updateArticle(JobArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleEnum deleteArticle(Integer id, Integer userId)
            throws ArticleException;

    public List<JobArticle> getArticleList(@Param("articleCondition") JobArticle articleCondition,
                                           @Param("dateCondition")Integer dateCondition);
    public Integer searchCount(@Param("articleCondition") JobArticle articleCondition,
                               @Param("dateCondition")Integer dateCondition);
    public JobArticle findArticleById(@Param("articleId") Integer articleId);
    public void incReadCount(JobArticle article);
    public void batchDel(@Param( "List" ) List List);
}
