package com.hzu.community.mapper;

import com.hzu.community.bean.HelpArticle;
import com.hzu.community.bean.LostArticle;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface HelpArticleMapper {
    //新增校园互助
//     使用jdbc的getGeneratedKeys获取数据库自增主键值
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into help_article(article_category_id,user_id," +
            "article_title,article_content,description," +
            "create_time,edit_time,tag) " +
            "values(#{articleCategory.articleCategoryId}," +
            "#{userInfo.userId}," +
            "#{articleTitle}," +
            "#{articleContent}," +
            "#{description}," +
            "#{createTime}," +
            "#{editTime}," +
            "#{tag}" +

            ")")
    public int add(HelpArticle article);

    //    更新帖子
    @Update("<script> " +
            "update help_article " +

            " <set> " +
            " <if test=\"articleCategory != null\"> article_category_id = #{articleCategory.articleCategoryId},</if> " +
            " <if test=\"articleImg != null\"> article_img = #{articleImg},</if> " +
            " <if test=\"articleTitle != null\"> article_title = #{articleTitle},</if> " +
            " <if test=\"articleContent != null\"> article_content = #{articleContent},</if> " +
            " <if test=\"editTime != null\"> edit_time = #{editTime},</if> " +
            " <if test=\"description != null\"> description = #{description},</if> " +
            " <if test=\"top != null\"> top = #{top},</if> " +
            " <if test=\"tag != null\"> tag = #{tag}</if> " +
            " </set> " +
            "where id = #{id}" +
            " </script> ")
    public int update(HelpArticle Article);

    @Select("<script> " +
            "SELECT * " +
            "from help_article " +
            " <where> " +
            " <if test=\"articleCondition.articleCategory != null and articleCondition.articleCategory.articleCategoryId != null\">and article_category_id = #{articleCondition.articleCategory.articleCategoryId}</if> " +
            " <if test=\"articleCondition.userInfo != null and articleCondition.userInfo.userId != null\">and user_id = #{articleCondition.userInfo.userId}</if> " +
            " <if test=\"articleCondition.articleTitle != null \">and article_title like '%${articleCondition.articleTitle}%'</if> " +
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
            @Result(column="article_title",property="articleTitle"),
            @Result(column="article_content",property="articleContent"),
            @Result(column="create_time",property="createTime"),
            @Result(column="edit_time",property="editTime"),
            @Result(column="article_img",property="articleImg"),
            @Result(column="description",property="description"),
            @Result(column="tag",property="tag"),
            @Result(column="top",property="top"),
            @Result(column="read_count",property="readCount")
    })
    public List<HelpArticle> getArticleList(@Param("articleCondition") HelpArticle articleCondition,
                                            @Param("dateCondition")Integer dateCondition);

    @Select("<script> " +
            "SELECT * " +
            "from help_article " +
            " <where> " +
            " <if test=\"articleId != null\">and id = #{articleId}</if> " +
            " </where> " +
            "order by edit_time desc" +
            " </script> ")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="article_category_id",property="articleCategory",one = @One(select = "com.hzu.community.mapper.ArticleCategoryMapper.findArticleCategoryById",fetchType= FetchType.EAGER)),
            @Result(column="user_id",property="userInfo",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById",fetchType= FetchType.EAGER)),
            @Result(column="article_title",property="articleTitle"),
            @Result(column="article_content",property="articleContent"),
            @Result(column="create_time",property="createTime"),
            @Result(column="edit_time",property="editTime"),
            @Result(column="article_img",property="articleImg"),
            @Result(column="description",property="description"),
            @Result(column="tag",property="tag"),
            @Result(column="top",property="top"),
            @Result(column="read_count",property="readCount")
    })
    public HelpArticle findArticleById(@Param("articleId") Integer articleId);

    @Delete("delete from help_article where id = #{articleId}")
    public int deleById(@Param("articleId") Integer articleId);

    //    批量删除文章
    @Delete( " <script>" +
            "delete from help_article where id in" +
            " <foreach collection='List' open='(' item='article' separator=',' close=')'> #{article.id}</foreach> "+
            " </script>" )
    public void batchDel(@Param( "List" ) List  List);

    @Update("update help_article set read_count = read_count+1 where id = #{id}")
    public void incReadCount(HelpArticle article);

    @Select("<script> " +
            "SELECT count(1) " +
            "from help_article " +
            " <where> " +
            " <if test=\"articleCondition.articleCategory != null and articleCondition.articleCategory.articleCategoryId != null\">and article_category_id = #{articleCondition.articleCategory.articleCategoryId}</if> " +
            " <if test=\"articleCondition.userInfo != null and articleCondition.userInfo.userId != null\">and user_id = #{articleCondition.userInfo.userId}</if> " +
            " <if test=\"articleCondition.articleTitle != null \">and article_title like '%${articleCondition.articleTitle}%'</if> " +
            " <if test=\"articleCondition.tag != null \">and tag like '%${articleCondition.tag}%'</if> " +
            " <if test=\"dateCondition != null\">and DATE_SUB(CURDATE(), INTERVAL #{dateCondition} DAY) <![CDATA[<=date(EDIT_TIME)]]></if>" +
            " </where> " +
            "order by edit_time desc" +
            " </script> ")

    public Integer searchCount(@Param("articleCondition") HelpArticle articleCondition,
                                            @Param("dateCondition")Integer dateCondition);




}
