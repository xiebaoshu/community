package com.hzu.community.mapper;

import com.hzu.community.bean.ArticleCategory;
import com.hzu.community.bean.LostArticle;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface LostArticleMapper {

//新增失物招领
//     使用jdbc的getGeneratedKeys获取数据库自增主键值
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into lost_article(article_category_id,user_id,area_id,item_category_id," +
            "article_title,phone,article_content,description," +
            "edit_time,tag) " +
            "values(#{articleCategory.articleCategoryId}," +
            "#{userInfo.userId}," +
            "#{area.areaId}," +
            "#{itemCategory.itemCategoryId}," +
            "#{articleTitle}," +
            "#{phone}," +
            "#{articleContent}," +
            "#{description}," +
            "#{editTime}," +
            "#{tag}" +

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
            " <if test=\"tag != null\"> tag = #{tag},</if> " +
            " <if test=\"articleContent != null\"> article_content = #{articleContent},</if> " +
            " <if test=\"editTime != null\"> edit_time = #{editTime},</if> " +
            " <if test=\"description != null\"> description = #{description},</if> " +
            " <if test=\"finishTime != null\"> finish_time = #{finishTime},</if> " +
            " <if test=\"top != null\"> top = #{top},</if> " +
            " <if test=\"finishUser != null\"> finisher_id = #{finishUser.userId}</if> " +
            " </set> " +
            "where id = #{id}" +
            " </script> ")
    public int updatelost(LostArticle lostArticle);




    @Select("<script> " +
            "SELECT * " +
            "from lost_article " +
            " <where> " +
            " <if test=\"articleCondition.articleCategory != null and articleCondition.articleCategory.articleCategoryId != null\">and article_category_id = #{articleCondition.articleCategory.articleCategoryId}</if> " +
            " <if test=\"articleCondition.userInfo != null and articleCondition.userInfo.userId != null\">and user_id = #{articleCondition.userInfo.userId}</if> " +
            " <if test=\"articleCondition.articleTitle != null \">and article_title like '%${articleCondition.articleTitle}%' or description like '%${articleCondition.articleTitle}%'</if> " +
            " <if test=\"articleCondition.area != null and articleCondition.area.areaId != null\">and area_id = #{articleCondition.area.areaId}</if> " +
            " <if test=\"articleCondition.itemCategory != null and articleCondition.itemCategory.itemCategoryId != null\">and item_category_id = #{articleCondition.itemCategory.itemCategoryId}</if> " +
            " <if test=\"articleCondition.tag != null \">and tag like '%${articleCondition.tag}%'</if> " +
            " <if test=\"dateCondition != null\">and DATE_SUB(CURDATE(), INTERVAL #{dateCondition} DAY) <![CDATA[<=date(EDIT_TIME)]]></if>" +
            " </where> " +
            " <choose>" +
            " <when test=\"articleCondition.sort != null and articleCondition.sort!= ''\">" +
            "     order by top desc,${articleCondition.sort} desc\n" +
            " </when>" +
            " <otherwise>" +
            "     order by top desc,edit_time desc" +
            " </otherwise>" +
            " </choose>" +
            " </script> ")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="article_category_id",property="articleCategory",one = @One(select = "com.hzu.community.mapper.ArticleCategoryMapper.findArticleCategoryById",fetchType= FetchType.EAGER)),
            @Result(column="user_id",property="userInfo",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById",fetchType= FetchType.EAGER)),
            @Result(column="area_id",property="area",one = @One(select = "com.hzu.community.mapper.AreaMapper.findAreaById")),
            @Result(column="item_category_id",property="itemCategory",one = @One(select = "com.hzu.community.mapper.ItemCategoryMapper.findItemCategoryById")),
            @Result(column="article_title",property="articleTitle"),
            @Result(column="phone",property="phone"),
            @Result(column="tag",property="tag"),
            @Result(column="article_content",property="articleContent"),
            @Result(column="edit_time",property="editTime"),
            @Result(column="finish_time",property="finishTime"),
            @Result(column="finisher_id",property="finishUser",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="article_img",property="articleImg"),
            @Result(column="description",property="description"),
            @Result(column="top",property="top"),
            @Result(column="read_count",property="readCount")
    })
    public List<LostArticle> getArticleList(@Param("articleCondition") LostArticle articleCondition,
                                            @Param("dateCondition")Integer dateCondition);




    @Select("<script> " +
            "SELECT * " +
            "from lost_article " +
            " <where> " +
            " <if test=\"articleId != null\">and id = #{articleId}</if> " +
            " </where> " +
            "order by edit_time desc" +
            " </script> ")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="article_category_id",property="articleCategory",one = @One(select = "com.hzu.community.mapper.ArticleCategoryMapper.findArticleCategoryById",fetchType= FetchType.EAGER)),
            @Result(column="user_id",property="userInfo",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById",fetchType= FetchType.EAGER)),
            @Result(column="area_id",property="area",one = @One(select = "com.hzu.community.mapper.AreaMapper.findAreaById")),
            @Result(column="item_category_id",property="itemCategory",one = @One(select = "com.hzu.community.mapper.ItemCategoryMapper.findItemCategoryById")),
            @Result(column="article_title",property="articleTitle"),
            @Result(column="phone",property="phone"),
            @Result(column="article_content",property="articleContent"),
            @Result(column="edit_time",property="editTime"),
            @Result(column="finish_time",property="finishTime"),
            @Result(column="tag",property="tag"),
            @Result(column="finisher_id",property="finishUser",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById")),
            @Result(column="article_img",property="articleImg"),
            @Result(column="description",property="description"),
            @Result(column="top",property="top"),
            @Result(column="read_count",property="readCount")
    })
    public LostArticle findArticleById(@Param("articleId") Integer articleId);


    @Delete("delete from lost_article where id = #{articleId}")
    public int deleById(@Param("articleId") Integer articleId);

    //    批量删除
    @Delete( " <script>" +
            "delete from lost_article where id in" +
            " <foreach collection='List' open='(' item='article' separator=',' close=')'> #{article.id}</foreach> "+
            " </script>" )
    public void batchDel(@Param( "List" ) List  List);

    @Update("update lost_article set read_count = read_count+1 where id = #{id}")
    public void incReadCount(LostArticle lostArticle);

    @Select("<script> " +
            "SELECT count(1) " +
            "from lost_article " +
            " <where> " +
            " <if test=\"articleCondition.articleCategory != null and articleCondition.articleCategory.articleCategoryId != null\">and article_category_id = #{articleCondition.articleCategory.articleCategoryId}</if> " +
            " <if test=\"articleCondition.userInfo != null and articleCondition.userInfo.userId != null\">and user_id = #{articleCondition.userInfo.userId}</if> " +
            " <if test=\"articleCondition.articleTitle != null \">and article_title like '%${articleCondition.articleTitle}%' or description like '%${articleCondition.articleTitle}%'</if> " +
            " <if test=\"articleCondition.area != null and articleCondition.area.areaId != null\">and area_id = #{articleCondition.area.areaId}</if> " +
            " <if test=\"articleCondition.tag != null \">and tag like '%${articleCondition.tag}%'</if> " +
            " <if test=\"articleCondition.itemCategory != null and articleCondition.itemCategory.itemCategoryId != null\">and item_category_id = #{articleCondition.itemCategory.itemCategoryId}</if> " +
            " <if test=\"dateCondition != null\">and DATE_SUB(CURDATE(), INTERVAL #{dateCondition} DAY) <![CDATA[<=date(EDIT_TIME)]]></if>" +
            " </where> " +
            "order by edit_time desc" +
            " </script> ")
    public Integer searchCount(@Param("articleCondition") LostArticle articleCondition,
                                            @Param("dateCondition")Integer dateCondition);
}
