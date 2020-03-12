package com.hzu.community.mapper;

import com.hzu.community.bean.Area;
import com.hzu.community.bean.ArticleCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleCategoryMapper {
   /* @Select("select * from article_category where article_category_id=#{id}")
    @ResultMap("com.hzu.community.bean.ArticleCategory")
    public ArticleCategory findArticleCategoryByid(@Param("id") int id);*/

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
