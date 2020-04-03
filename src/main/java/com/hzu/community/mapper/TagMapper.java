package com.hzu.community.mapper;

import com.hzu.community.bean.ArticleCategory;
import com.hzu.community.bean.LostArticle;
import com.hzu.community.bean.Tag;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
@Mapper
public interface TagMapper {



    @Select("<script> " +
            "SELECT * " +
            "from tag " +
            " <where> " +
            " <if test=\"parentId != null\"> and par_id = #{parentId}</if> " +
            " </where> " +
            " </script> ")
    public List<Tag> tagList(@Param("parentId") Integer parentId);



    @Select("<script> " +
            "SELECT * " +
            "from tag " +
            "where par_id is null" +
            " </script> ")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="name",property="name"),
            @Result(column="id",property="tagList",many = @Many(select = "com.hzu.community.mapper.TagMapper.tagList"))
    })
    public List<Tag> allTag();
}
