package com.hzu.community.service;

import com.hzu.community.dto.JobFair;
import com.hzu.community.dto.NoticeDto;

import java.util.List;

public interface InformationService {
    public List<JobFair> JobFairList();
    public List<NoticeDto> NoticeDtoList();
}
