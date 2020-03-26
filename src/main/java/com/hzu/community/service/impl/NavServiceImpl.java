package com.hzu.community.service.impl;

import com.hzu.community.bean.Nav;
import com.hzu.community.mapper.NavMapper;
import com.hzu.community.service.NavService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavServiceImpl implements NavService {
    @Autowired
    private NavMapper navMapper;
    @Override
    public List<Nav> navDefault() {
        return navMapper.navDefault();
    }
}
