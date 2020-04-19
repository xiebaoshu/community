package com.hzu.community.mapper;

import com.hzu.community.bean.SchoolArticle;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface SchoolArticleMapper {
    //新增校园公告
//     使用jdbc的getGeneratedKeys获取数据库自增主键值
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into school_article(article_category_id,user_id," +
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
    public int add(SchoolArticle article);

    //    更新帖子
    @Update("<script> " +
            "update school_article " +

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
    public int update(SchoolArticle Article);

    @Select("<script> " +
            "SELECT * " +
            "from school_article " +
            " <where> " +
            " <if test=\"articleCondition.articleCategory != null and articleCondition.articleCategory.articleCategoryId != null\">and article_category_id = #{articleCondition.articleCategory.articleCategoryId}</if> " +
            " <if test=\"articleCondition.userInfo != null and articleCondition.userInfo.userId != null\">and user_id = #{articleCondition.userInfo.userId}</if> " +
            " <if test=\"articleCondition.articleTitle != null \">and article_title like '%${articleCondition.articleTitle}%'</if> " +
            " <if test=\"articleCondition.tag != null \">and tag like '%${articleCondition.tag}%'</if> " +
            " <if test=\"dateCondition != null\">and DATE_SUB(CURDATE(), INTERVAL #{dateCondition} DAY) <![CDATA[<=date(EDIT_TIME)]]></if>" +
            " </where> " +
            "order by top desc,edit_time desc" +
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
    public List<SchoolArticle> getArticleList(@Param("articleCondition") SchoolArticle articleCondition,
                                            @Param("dateCondition")Integer dateCondition);

    @Select("<script> " +
            "SELECT * " +
            "from school_article " +
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
    public SchoolArticle findArticleById(@Param("articleId") Integer articleId);

    @Delete("delete from school_article where id = #{articleId}")
    public int deleById(@Param("articleId") Integer articleId);

    //    批量删除
    @Delete( " <script>" +
            "delete from school_article where id in" +
            " <foreach collection='List' open='(' item='article' separator=',' close=')'> #{article.id}</foreach> "+
            " </script>" )
    public void batchDel(@Param( "List" ) List  List);

    @Update("update school_article set read_count = read_count+1 where id = #{id}")
    void incReadCount(SchoolArticle article);

    @Select("<script> " +
            "SELECT count(1) " +
            "from school_article " +
            " <where> " +
            " <if test=\"articleCondition.articleCategory != null and articleCondition.articleCategory.articleCategoryId != null\">and article_category_id = #{articleCondition.articleCategory.articleCategoryId}</if> " +
            " <if test=\"articleCondition.userInfo != null and articleCondition.userInfo.userId != null\">and user_id = #{articleCondition.userInfo.userId}</if> " +
            " <if test=\"articleCondition.articleTitle != null \">and article_title like '%${articleCondition.articleTitle}%'</if> " +
            " <if test=\"articleCondition.tag != null \">and tag like '%${articleCondition.tag}%'</if> " +
            " <if test=\"dateCondition != null\">and DATE_SUB(CURDATE(), INTERVAL #{dateCondition} DAY) <![CDATA[<=date(EDIT_TIME)]]></if>" +
            " </where> " +
            "order by edit_time desc" +
            " </script> ")
    public Integer searchCount(@Param("articleCondition") SchoolArticle articleCondition,
                                              @Param("dateCondition")Integer dateCondition);
    
}
