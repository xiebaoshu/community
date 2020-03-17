package com.hzu.community.mapper;

import com.hzu.community.bean.ArticleCategory;
import com.hzu.community.bean.LostArticle;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

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


//    更新帖子
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



    /** @Param("rowIndex")从第几行开始取数据
	 * @Param("pageSize")返回的条数*/
    @Select("<script> " +
            "SELECT * " +
            "from lost_article " +
            " <where> " +
            " <if test=\"articleCondition.articleCategory != null and articleCondition.articleCategory.articleCategoryId != null\">and article_category_id = #{articleCondition.articleCategory.articleCategoryId}</if> " +
            " <if test=\"articleCondition.userInfo != null and articleCondition.userInfo.userId != null\">and user_id = #{articleCondition.userInfo.userId}</if> " +
            " <if test=\"articleCondition.articleTitle != null \">and article_title like '%${articleCondition.articleTitle}%'</if> " +
            " <if test=\"articleCondition.area != null and articleCondition.area.areaId != null\">and area_id = #{articleCondition.area.areaId}</if> " +
            " <if test=\"articleCondition.itemCategory != null and articleCondition.itemCategory.itemCategoryId != null\">and item_category_id = #{articleCondition.itemCategory.itemCategoryId}</if> " +
            " <if test=\"dateCondition != null\">and DATE_SUB(CURDATE(), INTERVAL #{dateCondition} DAY) <![CDATA[<=date(CREATE_TIME)]]></if> " +
            " </where> " +

            " </script> ")
    @Results({
            @Result(id=true,column="lost_article_id",property="lostArticleId"),
            @Result(column="article_category_id",property="articleCategory",one = @One(select = "com.hzu.community.mapper.ArticleCategoryMapper.findArticleCategoryById",fetchType= FetchType.EAGER)),
            @Result(column="user_id",property="userInfo",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById",fetchType= FetchType.EAGER)),
            @Result(column="area_id",property="area",one = @One(select = "com.hzu.community.mapper.AreaMapper.findAreaById")),
            @Result(column="item_category_id",property="itemCategory",one = @One(select = "com.hzu.community.mapper.ItemCategoryMapper.findItemCategoryById")),
            @Result(column="article_title",property="articleTitle"),
            @Result(column="phone",property="phone"),
            @Result(column="article_content",property="articleContent"),
            @Result(column="area_detail",property="areaDetail"),
            @Result(column="item_detail",property="itemDetail"),
            @Result(column="create_time",property="createTime"),
            @Result(column="finish_time",property="finishTime"),
            @Result(column="finisher_id",property="finishUser",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="article_img",property="articleImg"),
            @Result(column="finish_time",property="finishTime"),
            @Result(column="finisher_id",property="finishUser",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById"))
    })
    public List<LostArticle> getArticleList(@Param("articleCondition") LostArticle articleCondition,
                                            @Param("dateCondition")Integer dateCondition);




    @Select("<script> " +
            "SELECT * " +
            "from lost_article " +
            " <where> " +
            " <if test=\"articleId != null\">and lost_article_id = #{articleId}</if> " +
            " </where> " +
            " </script> ")
    @Results({
            @Result(id=true,column="lost_article_id",property="lostArticleId"),
            @Result(column="article_category_id",property="articleCategory",one = @One(select = "com.hzu.community.mapper.ArticleCategoryMapper.findArticleCategoryById",fetchType= FetchType.EAGER)),
            @Result(column="user_id",property="userInfo",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById",fetchType= FetchType.EAGER)),
            @Result(column="area_id",property="area",one = @One(select = "com.hzu.community.mapper.AreaMapper.findAreaById")),
            @Result(column="item_category_id",property="itemCategory",one = @One(select = "com.hzu.community.mapper.ItemCategoryMapper.findItemCategoryById")),
            @Result(column="article_title",property="articleTitle"),
            @Result(column="phone",property="phone"),
            @Result(column="article_content",property="articleContent"),
            @Result(column="area_detail",property="areaDetail"),
            @Result(column="item_detail",property="itemDetail"),
            @Result(column="create_time",property="createTime"),
            @Result(column="finish_time",property="finishTime"),
            @Result(column="finisher_id",property="finishUser",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="article_img",property="articleImg"),
            @Result(column="finish_time",property="finishTime"),
            @Result(column="finisher_id",property="finishUser",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById"))
    })
    public LostArticle findArticleById(@Param("articleId") Integer articleId);


    @Delete("delete from lost_article where lost_article_id = #{articleId}")
    public int deleById(@Param("articleId") Integer articleId);
}
