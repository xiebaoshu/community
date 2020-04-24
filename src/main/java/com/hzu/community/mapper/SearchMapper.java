package com.hzu.community.mapper;

import com.hzu.community.dto.SearchDto;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.Date;
import java.util.List;

@Mapper
public interface SearchMapper {
    @Select("<script> " +
            "SELECT * FROM\n" +
            "(select id,article_category_id,user_id,article_img,article_title,edit_time,description,top,read_count from lost_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,edit_time,description,top,read_count from second_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,edit_time,description,top,read_count from help_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,edit_time,description,top,read_count from job_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,edit_time,description,top,read_count from school_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,edit_time,description,top,read_count from company_article) as a\n" +
            "<where>" +
            " <if test=\"articleTitle!= null\">and article_title LIKE '%${articleTitle}%'</if> " +
            " <if test=\"articleCategory!= null and articleCategory.articleCategoryId!= null\">and article_category_id =#{articleCategory.articleCategoryId}</if> " +
            " <if test=\"top!= null \">and top =#{top}</if> " +
            " <if test=\"userInfo != null and userInfo.userId != null\">and user_id = #{userInfo.userId}</if> " +
            " <if test=\"date != null\">and DATE_SUB(CURDATE(), INTERVAL #{date} DAY) <![CDATA[<=date(EDIT_TIME)]]></if>" +

            "</where>" +
            " <choose>" +
            " <when test=\"sort != null and sort!= ''\">" +
            "     order by top desc,${sort} desc\n" +
            " </when>" +
            " <otherwise>" +
            "     order by top desc,edit_time desc" +
            " </otherwise>" +
            " </choose>" +
            "</script>")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="article_category_id",property="articleCategory",one = @One(select = "com.hzu.community.mapper.ArticleCategoryMapper.findArticleCategoryById",fetchType= FetchType.EAGER)),
            @Result(column="user_id",property="userInfo",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById",fetchType= FetchType.EAGER)),
            @Result(column="article_title",property="articleTitle"),
            @Result(column="edit_time",property="editTime"),
            @Result(column="article_img",property="articleImg"),
            @Result(column="description",property="description"),
            @Result(column="read_count",property="readCount"),
            @Result(column="top",property="top")
    })
    public List<SearchDto> getAll(SearchDto searchDto);


    @Select("<script> " +
            "SELECT count(1) FROM\n" +
            "(select id,article_category_id,user_id,article_img,article_title,edit_time,description,top,read_count from lost_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,edit_time,description,top,read_count from second_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,edit_time,description,top,read_count from help_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,edit_time,description,top,read_count from job_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,edit_time,description,top,read_count from school_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,edit_time,description,top,read_count from company_article) as a\n" +
            "<where>" +
            " <if test=\"articleTitle!= null\">and article_title LIKE '%${articleTitle}%'</if> " +
            " <if test=\"articleCategory!= null and articleCategory.articleCategoryId!= null\">and article_category_id =#{articleCategory.articleCategoryId}</if> " +
            " <if test=\"top!= null \">and top =#{top}</if> " +
            " <if test=\"userInfo != null and userInfo.userId != null\">and user_id = #{userInfo.userId}</if> " +
            " <if test=\"date != null\">and DATE_SUB(CURDATE(), INTERVAL #{date} DAY) <![CDATA[<=date(EDIT_TIME)]]></if>" +

            "</where>" +
            "order by edit_time DESC" +
            "</script>")
    public Integer getCount(SearchDto searchDto);
}
