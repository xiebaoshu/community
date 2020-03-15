package com.hzu.community.service.impl;

import com.hzu.community.bean.ArticleCategory;
import com.hzu.community.mapper.ArticleCategoryMapper;
import com.hzu.community.service.ArticleCategoryService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleCategoryServiceImpl implements ArticleCategoryService {
    @Autowired
    ArticleCategoryMapper articleCategoryMapper;
    @Override
    public List<ArticleCategory> getArticleCategories(@Param("parentId") Integer parentId){
        return articleCategoryMapper.getArticleCategory(parentId);
    }
}
