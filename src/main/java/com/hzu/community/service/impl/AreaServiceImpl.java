package com.hzu.community.service.impl;

import com.hzu.community.bean.Area;
import com.hzu.community.mapper.AreaMapper;
import com.hzu.community.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    AreaMapper areaMapper;

    @Override
    public List<Area> getAreaList(){
        return areaMapper.getArea();
    }
}
