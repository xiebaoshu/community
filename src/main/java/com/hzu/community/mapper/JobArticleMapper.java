package com.hzu.community.mapper;
import com.hzu.community.bean.JobArticle;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface JobArticleMapper {
    //新增兼职信息
//     使用jdbc的getGeneratedKeys获取数据库自增主键值
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into job_article(article_category_id,user_id," +
            "article_title,article_content,description," +
            "create_time,edit_time,tag,salary,knots) " +
            "values(#{articleCategory.articleCategoryId}," +
            "#{userInfo.userId}," +
            "#{articleTitle}," +
            "#{articleContent}," +
            "#{description}," +
            "#{createTime}," +
            "#{editTime}," +
            "#{tag}," +
            "#{salary.id}," +
            "#{knots}" +

            ")")
    public int add(JobArticle article);

    //    更新帖子
    @Update("<script> " +
            "update job_article " +
            " <set> " +
            " <if test=\"articleCategory != null\"> article_category_id = #{articleCategory.articleCategoryId},</if> " +
            " <if test=\"articleImg != null\"> article_img = #{articleImg},</if> " +
            " <if test=\"articleTitle != null\"> article_title = #{articleTitle},</if> " +
            " <if test=\"articleContent != null\"> article_content = #{articleContent},</if> " +
            " <if test=\"editTime != null\"> edit_time = #{editTime},</if> " +
            " <if test=\"description != null\"> description = #{description},</if> " +
            " <if test=\"tag != null\"> tag = #{tag},</if> " +
            " <if test=\"knots != null\"> knots = #{knots},</if> " +
            " <if test=\"top != null\"> top = #{top},</if> " +
            " <if test=\"salary != null\"> salary = #{salary.id}</if> " +
            " </set> " +
            "where id = #{id}" +
            " </script> ")
    public int update(JobArticle Article);

    @Select("<script> " +
            "SELECT * " +
            "from job_article " +
            " <where> " +
            " <if test=\"articleCondition.articleCategory != null and articleCondition.articleCategory.articleCategoryId != null\">and article_category_id = #{articleCondition.articleCategory.articleCategoryId}</if> " +
            " <if test=\"articleCondition.userInfo != null and articleCondition.userInfo.userId != null\">and user_id = #{articleCondition.userInfo.userId}</if> " +
            " <if test=\"articleCondition.articleTitle != null \">and article_title like '%${articleCondition.articleTitle}%'</if> " +
            " <if test=\"articleCondition.tag != null \">and tag like '%${articleCondition.tag}%'</if> " +
            " <if test=\"dateCondition != null\">and DATE_SUB(CURDATE(), INTERVAL #{dateCondition} DAY) <![CDATA[<=date(EDIT_TIME)]]></if>" +
            " <if test=\"articleCondition.knots != null \">and knots =#{articleCondition.knots}</if> " +
            " <if test=\"articleCondition.salary != null and articleCondition.salary.id != null \">and salary = #{articleCondition.salary.id}</if> " +
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
            @Result(column="read_count",property="readCount"),
            @Result(column="knots",property="knots"),
            @Result(column="top",property="top"),
            @Result(column="salary",property="salary",one = @One(select = "com.hzu.community.mapper.SalaryMapper.findSalaryById",fetchType= FetchType.EAGER))
    })
    public List<JobArticle> getArticleList(@Param("articleCondition") JobArticle articleCondition,
                                            @Param("dateCondition")Integer dateCondition);

    @Select("<script> " +
            "SELECT * " +
            "from job_article " +
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
            @Result(column="read_count",property="readCount"),
            @Result(column="knots",property="knots"),
            @Result(column="top",property="top"),
            @Result(column="salary",property="salary",one = @One(select = "com.hzu.community.mapper.SalaryMapper.findSalaryById",fetchType= FetchType.EAGER))
    })
    public JobArticle findArticleById(@Param("articleId") Integer articleId);

    @Delete("delete from job_article where id = #{articleId}")
    public int deleById(@Param("articleId") Integer articleId);
//    批量删除
    @Delete( " <script>" +
            "delete from job_article where id in" +
            " <foreach collection='List' open='(' item='article' separator=',' close=')'> #{article.id}</foreach> "+
            " </script>" )
    public void batchDel(@Param( "List" ) List  List);

    @Update("update job_article set read_count = read_count+1 where id = #{id}")
    public void incReadCount(JobArticle article);

    @Select("<script> " +
            "SELECT count(1) " +
            "from job_article " +
            " <where> " +
            " <if test=\"articleCondition.articleCategory != null and articleCondition.articleCategory.articleCategoryId != null\">and article_category_id = #{articleCondition.articleCategory.articleCategoryId}</if> " +
            " <if test=\"articleCondition.userInfo != null and articleCondition.userInfo.userId != null\">and user_id = #{articleCondition.userInfo.userId}</if> " +
            " <if test=\"articleCondition.articleTitle != null \">and article_title like '%${articleCondition.articleTitle}%'</if> " +
            " <if test=\"articleCondition.tag != null \">and tag like '%${articleCondition.tag}%'</if> " +
            " <if test=\"dateCondition != null\">and DATE_SUB(CURDATE(), INTERVAL #{dateCondition} DAY) <![CDATA[<=date(EDIT_TIME)]]></if>" +
            " <if test=\"articleCondition.knots != null \">and knots =#{articleCondition.knots}</if> " +
            " <if test=\"articleCondition.salary != null and articleCondition.salary.id != null \">and salary = #{articleCondition.salary.id}</if> " +
            " </where> " +
            "order by edit_time desc" +
            " </script> ")
    public Integer searchCount(@Param("articleCondition") JobArticle articleCondition,
                                           @Param("dateCondition")Integer dateCondition);

}
