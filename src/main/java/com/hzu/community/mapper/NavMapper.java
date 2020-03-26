package com.hzu.community.mapper;

import com.hzu.community.bean.Nav;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NavMapper {
    @Select("select * from nav_default")
    public List<Nav> navDefault();


}
