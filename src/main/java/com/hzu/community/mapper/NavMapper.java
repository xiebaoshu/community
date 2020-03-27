package com.hzu.community.mapper;

import com.hzu.community.bean.Nav;
import com.hzu.community.bean.UserInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NavMapper {
    @Select("select * from nav_default")
    public List<Nav> navDefault();

    @Select("select * from nav_diy where user_id = #{userId}")
    public List<Nav> navDiy(UserInfo userInfo);

    @Insert("insert into nav_diy(user_id,url,name,img_url) " +
            "values(#{userId},#{url},#{name},#{imgUrl})")
    public int add(Nav nav);

    @Delete("delete from nav_diy where id = #{id}")
    public void del(Integer id);


}
