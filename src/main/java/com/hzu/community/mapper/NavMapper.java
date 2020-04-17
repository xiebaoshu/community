package com.hzu.community.mapper;

import com.hzu.community.bean.LostArticle;
import com.hzu.community.bean.Nav;
import com.hzu.community.bean.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NavMapper {
    @Select("select * from nav_default")
    public List<Nav> navDefault();

    @Select("select * from nav_diy where user_id = #{userId}")
    public List<Nav> navDiy(UserInfo userInfo);

    @Select("select * from nav_diy where name = #{name} and user_id = #{userId}")
    public Nav findByName(Nav nav);

    @Select("select * from nav_default where name = #{name}")
    public Nav findDefaultByName(Nav nav);

    @Select("select * from nav_default where id = #{id}")
    public Nav findDefaultById(Nav nav);

    //     使用jdbc的getGeneratedKeys获取数据库自增主键值
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into nav_diy(user_id,url,name,img_url) " +
            "values(#{userId},#{url},#{name},#{imgUrl})")
    public int add(Nav nav);


    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into nav_default(url,name,img_url,description) " +
            "values(#{url},#{name},#{imgUrl},#{description})")
    public int addDefault(Nav nav);



    @Delete("delete from nav_diy where id = #{id}")
    public void del(Integer id);

    @Delete("delete from nav_default where id = #{id}")
    public void delDefault(Integer id);


    @Update("<script> " +
            "update nav_diy " +
            " <set> " +
            " <if test=\"url != null\"> url = #{url},</if> " +
            " <if test=\"name != null\"> name = #{name},</if> " +
            " <if test=\"imgUrl != null\"> img_url = #{imgUrl}</if> " +
            " </set> " +
            "where id = #{id}" +
            " </script> ")
    public int update(Nav nav);

    @Update("<script> " +
            "update nav_default " +
            " <set> " +
            " <if test=\"url != null\"> url = #{url},</if> " +
            " <if test=\"name != null\"> name = #{name},</if> " +
            " <if test=\"description != null\"> description = #{description},</if> " +
            " <if test=\"imgUrl != null\"> img_url = #{imgUrl}</if> " +
            " </set> " +
            "where id = #{id}" +
            " </script> ")
    public int updateDefault(Nav nav);


}
