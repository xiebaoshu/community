package com.hzu.community.mapper;
import com.hzu.community.bean.SecondArticle;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface SecondArticleMapper {
    //新增二手交易
//     使用jdbc的getGeneratedKeys获取数据库自增主键值
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into second_article(article_category_id,user_id," +
            "article_title,article_content,description," +
            "create_time,tag,price)" +
            "values(#{articleCategory.articleCategoryId}," +
            "#{userInfo.userId}," +
            "#{articleTitle}," +
            "#{articleContent}," +
            "#{description}," +
            "#{createTime}," +
            "#{tag}," +
            "#{price}" +
            ")")
    public int add(SecondArticle article);


    //    更新帖子
    @Update("<script> " +
            "update second_article " +

            " <set> " +
            " <if test=\"articleCategory != null\"> article_category_id = #{articleCategory.articleCategoryId},</if> " +
            " <if test=\"articleImg != null\"> article_img = #{articleImg},</if> " +
            " <if test=\"articleTitle != null\"> article_title = #{articleTitle},</if> " +
            " <if test=\"articleContent != null\"> article_content = #{articleContent},</if> " +
            " <if test=\"createTime != null\"> create_time = #{createTime},</if> " +
            " <if test=\"description != null\"> description = #{description},</if> " +
            " <if test=\"tag != null\"> tag = #{tag},</if> " +
            " <if test=\"price != null\"> price = #{price}</if> " +
            " </set> " +
            "where id = #{id}" +
            " </script> ")
    public int update(SecondArticle Article);


    @Select("<script> " +
            "SELECT * " +
            "from second_article " +
            " <where> " +
            " <if test=\"articleCondition.articleCategory != null and articleCondition.articleCategory.articleCategoryId != null\">and article_category_id = #{articleCondition.articleCategory.articleCategoryId}</if> " +
            " <if test=\"articleCondition.userInfo != null and articleCondition.userInfo.userId != null\">and user_id = #{articleCondition.userInfo.userId}</if> " +
            " <if test=\"articleCondition.articleTitle != null \">and article_title like '%${articleCondition.articleTitle}%'</if> " +
            " <if test=\"articleCondition.tag != null \">and tag like '%${articleCondition.tag}%'</if> " +
            " <if test=\"prePrice != null \">and price &gt;= #{prePrice} </if> " +
            " <if test=\"nextPrice != null \">and price &lt;= #{nextPrice}</if> " +
            " <if test=\"dateCondition != null\">and DATE_SUB(CURDATE(), INTERVAL #{dateCondition} DAY) <![CDATA[<=date(CREATE_TIME)]]></if>" +
            " </where> " +
            "order by create_time desc" +
            " </script> ")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="article_category_id",property="articleCategory",one = @One(select = "com.hzu.community.mapper.ArticleCategoryMapper.findArticleCategoryById",fetchType= FetchType.EAGER)),
            @Result(column="user_id",property="userInfo",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById",fetchType= FetchType.EAGER)),
            @Result(column="article_title",property="articleTitle"),
            @Result(column="article_content",property="articleContent"),
            @Result(column="create_time",property="createTime"),
            @Result(column="article_img",property="articleImg"),
            @Result(column="description",property="description"),
            @Result(column="tag",property="tag"),
            @Result(column="read_count",property="readCount"),
            @Result(column="price",property="price")
    })
    public List<SecondArticle> getArticleList(@Param("articleCondition") SecondArticle articleCondition,
                                            @Param("dateCondition")Integer dateCondition,
                                              @Param("prePrice")Double prePrice,
                                              @Param("nextPrice")Double nextPrice);

    @Select("<script> " +
            "SELECT * " +
            "from second_article " +
            " <where> " +
            " <if test=\"articleId != null\">and id = #{articleId}</if> " +
            " </where> " +
            "order by create_time desc" +
            " </script> ")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="article_category_id",property="articleCategory",one = @One(select = "com.hzu.community.mapper.ArticleCategoryMapper.findArticleCategoryById",fetchType= FetchType.EAGER)),
            @Result(column="user_id",property="userInfo",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById",fetchType= FetchType.EAGER)),
            @Result(column="article_title",property="articleTitle"),
            @Result(column="article_content",property="articleContent"),
            @Result(column="create_time",property="createTime"),
            @Result(column="article_img",property="articleImg"),
            @Result(column="description",property="description"),
            @Result(column="tag",property="tag"),
            @Result(column="read_count",property="readCount"),
            @Result(column="price",property="price")
    })
    public SecondArticle findArticleById(@Param("articleId") Integer articleId);

    @Delete("delete from second_article where id = #{articleId}")
    public int deleById(@Param("articleId") Integer articleId);

    @Update("update second_article set read_count = read_count+1 where id = #{id}")
    void incReadCount(SecondArticle article);

    @Select("<script> " +
            "SELECT count(1) " +
            "from second_article " +
            " <where> " +
            " <if test=\"articleCondition.articleCategory != null and articleCondition.articleCategory.articleCategoryId != null\">and article_category_id = #{articleCondition.articleCategory.articleCategoryId}</if> " +
            " <if test=\"articleCondition.userInfo != null and articleCondition.userInfo.userId != null\">and user_id = #{articleCondition.userInfo.userId}</if> " +
            " <if test=\"articleCondition.articleTitle != null \">and article_title like '%${articleCondition.articleTitle}%'</if> " +
            " <if test=\"articleCondition.tag != null \">and tag like '%${articleCondition.tag}%'</if> " +
            " <if test=\"prePrice != null \">and price &gt;= #{prePrice} </if> " +
            " <if test=\"nextPrice != null \">and price &lt;= #{nextPrice}</if> " +
            " <if test=\"dateCondition != null\">and DATE_SUB(CURDATE(), INTERVAL #{dateCondition} DAY) <![CDATA[<=date(CREATE_TIME)]]></if>" +
            " </where> " +
            "order by create_time desc" +
            " </script> ")
    public Integer searchCount(@Param("articleCondition") SecondArticle articleCondition,
                                              @Param("dateCondition")Integer dateCondition,
                                              @Param("prePrice")Double prePrice,
                                              @Param("nextPrice")Double nextPrice);
}
