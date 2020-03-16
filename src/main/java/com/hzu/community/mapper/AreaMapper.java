package com.hzu.community.mapper;

import com.hzu.community.bean.Area;
import com.hzu.community.bean.ArticleCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AreaMapper {
    @Select("select * from area")
    public List<Area> getArea();

    @Select("select * from area where area_id=#{id}")
    public Area findAreaById(@Param("id") Integer id);
}
