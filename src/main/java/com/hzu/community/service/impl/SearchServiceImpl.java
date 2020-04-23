package com.hzu.community.service.impl;

import com.hzu.community.dto.SearchDto;
import com.hzu.community.mapper.SearchMapper;
import com.hzu.community.service.SearchService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchMapper searchMapper;
    @Override
    public List<SearchDto> getAll(SearchDto searchDto) {
        return searchMapper.getAll(searchDto);
    }

    @Override
    public Integer getCount(SearchDto searchDto) {
        return searchMapper.getCount(searchDto);
    }
}
