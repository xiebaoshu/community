package com.hzu.community.mapper;

import com.hzu.community.bean.ArticleCategory;
import com.hzu.community.bean.LostArticle;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface LostArticleMapper {

//新增失物招领
//     使用jdbc的getGeneratedKeys获取数据库自增主键值
    @Options(useGeneratedKeys = true,keyProperty = "lostArticleId")
    @Insert("insert into lost_article(article_category_id,user_id,area_id,item_category_id," +
            "article_title,phone,article_content,area_detail,item_detail," +
            "create_time,finish_time) " +
            "values(#{articleCategory.articleCategoryId}," +
            "#{userInfo.userId}," +
            "#{area.areaId}," +
            "#{itemCategory.itemCategoryId}," +
            "#{articleTitle}," +
            "#{phone}," +
            "#{articleContent}," +
            "#{areaDetail}," +
            "#{itemDetail}," +
            "#{createTime}," +
            "#{finishTime}" +

            ")")
    public int addLostArticle(LostArticle lostArticle);


//    更新帖子里的失主信息
    @Update("<script> " +
            "update lost_article " +

            " <set> " +
            " <if test=\"articleCategory != null\"> article_category_id = #{articleCategory.articleCategoryId},</if> " +
            " <if test=\"area != null\"> area_id = #{area.areaId},</if> " +
            " <if test=\"itemCategory != null\"> item_category_id = #{itemCategory.itemCategoryId},</if> " +
            " <if test=\"articleImg != null\"> article_img = #{articleImg},</if> " +
            " <if test=\"articleTitle != null\"> article_title = #{articleTitle},</if> " +
            " <if test=\"phone != null\"> phone = #{phone},</if> " +
            " <if test=\"articleContent != null\"> article_content = #{articleContent},</if> " +
            " <if test=\"areaDetail != null\"> area_detail = #{areaDetail},</if> " +
            " <if test=\"itemDetail != null\"> item_detail = #{itemDetail},</if> " +
            " <if test=\"createTime != null\"> create_time = #{createTime},</if> " +
            " <if test=\"finishTime != null\"> finish_time = #{finishTime},</if> " +
            " <if test=\"finishUser != null\"> finisher_id = #{finishUser.userId}</if> " +
            " </set> " +
            "where lost_article_id = #{lostArticleId}" +
            " </script> ")
    public int updatelost(LostArticle lostArticle);


}
