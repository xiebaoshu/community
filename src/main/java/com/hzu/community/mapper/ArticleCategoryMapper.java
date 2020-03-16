package com.hzu.community.mapper;

import com.hzu.community.bean.Area;
import com.hzu.community.bean.ArticleCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface ArticleCategoryMapper {

    @Select("select * from article_category where article_category_id=#{id}")

    public ArticleCategory findArticleCategoryById(@Param("id") Integer id);

    @Select("<script> " +
            "SELECT * " +
            "from article_category " +
            " <where> " +
            " <if test=\"parentId == null\">and parent_id is null</if> " +
            " <if test=\"parentId != null\"> and parent_id = #{parentId}</if> " +
            " </where> " +
            " </script> ")
    public List<ArticleCategory> getArticleCategory(@Param("parentId") Integer parentId);
}
