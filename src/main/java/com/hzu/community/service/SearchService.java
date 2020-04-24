package com.hzu.community.service;

import com.hzu.community.dto.SearchDto;

import java.util.List;

public interface SearchService {
    public List<SearchDto> getAll(SearchDto searchDto);
    public List<SearchDto> getTOP3(SearchDto searchDto);
    public Integer getCount(SearchDto searchDto);
}
