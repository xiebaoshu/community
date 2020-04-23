package com.hzu.community.mapper;

import com.hzu.community.bean.Area;
import com.hzu.community.bean.ArticleCategory;
import com.hzu.community.bean.SchoolArticle;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface ArticleCategoryMapper {

    @Select("select * from article_category where article_category_id=#{id}")

    public ArticleCategory findArticleCategoryById(@Param("id") Integer id);

//    根据parentId判断获取一级还是二级分类
    @Select("<script> " +
            "SELECT * " +
            "from article_category " +
            " <where> " +
            " <if test=\"parentId == null\">and parent_id is null</if> " +
            " <if test=\"parentId != null\"> and parent_id = #{parentId}</if> " +
            " </where> " +
            " </script> ")
    public List<ArticleCategory> getArticleCategory(@Param("parentId") Integer parentId);

    @Select("select * from article_category " +
            "where article_category_name=#{articleCategoryName} and parent_id = #{parentId}")
    public ArticleCategory findByName(ArticleCategory articleCategory);

    //     使用jdbc的getGeneratedKeys获取数据库自增主键值
    @Options(useGeneratedKeys = true,keyProperty = "articleCategoryId")
    @Insert("insert into article_category(article_category_name,parent_id)" +
            "values(#{articleCategoryName},#{parentId})")
    public int add(ArticleCategory articleCategory);

    @Update("<script> " +
            "update article_category" +
            " <set> " +
            " <if test=\"articleCategoryName != null\"> article_category_name = #{articleCategoryName},</if> " +
            " <if test=\"parentId != null\"> parent_id = #{parentId},</if> " +
            " </set> " +
            "where article_category_id = #{articleCategoryId}" +
            " </script> ")
    public int update(ArticleCategory articleCategory);

    @Delete("delete from article_category where article_category_id = #{id}")
    public void del(Integer id);

//    用于获取文章标签列表，用于管理员界面
    @Select("<script> " +
            "SELECT * " +
            "from article_category " +
            "where parent_id is null" +
            " </script> ")
    @Results({
            @Result(id=true,column="article_category_id",property="articleCategoryId"),
            @Result(column="article_category_name",property="articleCategoryName"),
            @Result(column="article_category_id",property="tagList",many = @Many(select = "com.hzu.community.mapper.TagMapper.allTag"))
    })
    public List<ArticleCategory> articleTags();

    //    用于获取文章类别列表,用于管理员界面
    @Select("<script> " +
            "SELECT * " +
            "from article_category " +
            "where parent_id is null" +
            " </script> ")
    @Results({
            @Result(id=true,column="article_category_id",property="articleCategoryId"),
            @Result(column="article_category_name",property="articleCategoryName"),
            @Result(column="article_category_id",property="typeList",many = @Many(select = "com.hzu.community.mapper.ArticleCategoryMapper.getArticleCategory"))
    })
    public List<ArticleCategory> categoryList();




}
