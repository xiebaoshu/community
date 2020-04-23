package com.hzu.community.service;

import com.hzu.community.bean.ArticleCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleCategoryService {
    public ArticleCategory findArticleCategoryById(@Param("id") Integer id);
    public List<ArticleCategory> getArticleCategories(@Param("parentId") Integer parentId);

    public ArticleCategory findByName(ArticleCategory articleCategory);

    public int add(ArticleCategory articleCategory);
    public int update(ArticleCategory articleCategory);
    public void del(Integer id) throws RuntimeException;

    //    用于获取文章标签列表，用于管理员界面
    public List<ArticleCategory> articleTags();
    //    用于获取文章类别列表,用于管理员界面
    public List<ArticleCategory> categoryList();

}
