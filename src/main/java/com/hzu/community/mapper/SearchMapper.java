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
            "(select id,article_category_id,user_id,article_img,article_title,create_time,description from lost_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,create_time,description from second_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,create_time,description from help_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,create_time,description from job_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,create_time,description from school_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,create_time,description from company_article) as a\n" +
            "<where>" +
                " <if test=\"search!= null\">and article_title LIKE '%${search}%' or description LIKE '%${search}%'</if> " +
            " <if test=\"dateCondition != null\">and DATE_SUB(CURDATE(), INTERVAL #{dateCondition} DAY) <![CDATA[<=date(CREATE_TIME)]]></if>" +

            "</where>" +
            "order by create_time DESC" +
            "</script>")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="article_category_id",property="articleCategory",one = @One(select = "com.hzu.community.mapper.ArticleCategoryMapper.findArticleCategoryById",fetchType= FetchType.EAGER)),
            @Result(column="user_id",property="userInfo",one = @One(select = "com.hzu.community.mapper.UserInfoMapper.findUserInfoById",fetchType= FetchType.EAGER)),
            @Result(column="article_title",property="articleTitle"),
            @Result(column="create_time",property="createTime"),
            @Result(column="article_img",property="articleImg"),
            @Result(column="description",property="description")
    })
    public List<SearchDto> getAll(@Param("search") String search,
                                  @Param("dateCondition") Integer dateCondition);


    @Select("<script> " +
            "SELECT count(1) FROM\n" +
            "(select id,article_category_id,user_id,article_img,article_title,create_time,description from lost_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,create_time,description from second_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,create_time,description from help_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,create_time,description from job_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,create_time,description from school_article \n" +
            "union all\n" +
            "select id,article_category_id,user_id,article_img,article_title,create_time,description from company_article) as a\n" +
            "<where>" +
            " <if test=\"search!= null\">and article_title LIKE '%${search}%' or description LIKE '%${search}%'</if> " +
            " <if test=\"dateCondition != null\">and DATE_SUB(CURDATE(), INTERVAL #{dateCondition} DAY) <![CDATA[<=date(CREATE_TIME)]]></if>" +

            "</where>" +
            "order by create_time DESC" +
            "</script>")
    public Integer getCount(@Param("search") String search,
                                  @Param("dateCondition") Integer dateCondition);
}
