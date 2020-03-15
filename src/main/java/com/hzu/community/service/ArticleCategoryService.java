package com.hzu.community.service;

import com.hzu.community.bean.ArticleCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleCategoryService {
    public List<ArticleCategory> getArticleCategories(@Param("parentId") Integer parentId);
}
