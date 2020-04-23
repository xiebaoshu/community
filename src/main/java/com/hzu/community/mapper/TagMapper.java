package com.hzu.community.mapper;

import com.hzu.community.bean.ArticleCategory;
import com.hzu.community.bean.LostArticle;
import com.hzu.community.bean.Tag;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
@Mapper
public interface TagMapper {
//    二级标签列表
    @Select("<script> " +
            "SELECT * " +
            "from tag " +
            " <where> " +
            " <if test=\"parentId != null\"> and par_id = #{parentId}</if> " +
            " </where> " +
            " </script> ")
    public List<Tag> tagList(@Param("parentId") Integer parentId);


//    获取文章模块下的所有标签列表（包含二级标签）
    //articleParCategory 文章类型模块id
    @Select("<script> " +
            "SELECT * " +
            "from tag " +
            "where par_id is null and article_par_category=#{articleParCategory}" +
            " </script> ")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="name",property="name"),
            @Result(column="id",property="tagList",many = @Many(select = "com.hzu.community.mapper.TagMapper.tagList"))
    })
    public List<Tag> allTag(@Param("articleParCategory") Integer articleParCategory);


    @Select("<script> " +
            "SELECT * " +
            "from tag " +
            "where name=#{name} and article_par_category = #{articleParCategory}" +
            " </script> ")
    public Tag findByName(Tag tag);

    @Select("<script> " +
            "SELECT * " +
            "from tag " +
            "where id = #{id}" +
            " </script> ")
    public Tag findById(@Param("id") Integer id);

    //     使用jdbc的getGeneratedKeys获取数据库自增主键值
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into tag(name,par_id,article_par_category)" +
            "values(#{name},#{parId},#{articleParCategory})")
    public int add(Tag tag);

    @Update("<script> " +
            "update tag" +
            " <set> " +
            " <if test=\"name != null\"> name = #{name},</if> " +
            " <if test=\"parId != null\"> par_id = #{parId},</if> " +
            " <if test=\"articleParCategory != null\"> article_par_category = #{articleParCategory},</if> " +
            " </set> " +
            "where id = #{id}" +
            " </script> ")
    public int update(Tag tag);

    @Delete("delete from tag where id = #{id}")
    public void del(Integer id);

    @Delete("delete from tag where par_id = #{parentId}")
    public void delList(Integer parentId);
}
