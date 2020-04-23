package com.hzu.community.service;

import com.hzu.community.bean.CompanyArticle;

import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.exceptions.ArticleException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyArticleService {
    public ArticleEnum saveArticle(CompanyArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleEnum updateArticle(CompanyArticle article, ImageHolder imageHolder)
            throws ArticleException;
    public ArticleEnum deleteArticle(Integer id, Integer userId)
            throws ArticleException;
//    查询文章列表
    public List<CompanyArticle> getArticleList(@Param("articleCondition") CompanyArticle articleCondition,
                                               @Param("dateCondition")Integer dateCondition);
//    查询文章列表的文章数
    public Integer searchCount(@Param("articleCondition") CompanyArticle articleCondition,
                               @Param("dateCondition")Integer dateCondition);

    public CompanyArticle findArticleById(@Param("articleId") Integer articleId);
//    阅读量
    public void incReadCount(CompanyArticle article);
//    批量删除
    public void batchDel(@Param( "List" ) List List);
}
