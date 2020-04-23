package com.hzu.community.service;

import com.hzu.community.bean.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AreaService {

    public List<Area> getAreaList();
    public Area findAreaById(@Param("id") Integer id);
}
