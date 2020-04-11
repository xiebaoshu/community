package com.hzu.community.service.impl;

import com.hzu.community.bean.Tag;
import com.hzu.community.mapper.TagMapper;
import com.hzu.community.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Override
    public List<Tag> allTag(Integer articleParCategory) {
        return tagMapper.allTag(articleParCategory);
    }

    @Override
    public List<Tag> tagList(Integer parentId) {
        return tagMapper.tagList(parentId);
    }

    @Override
    public Tag findByName(Tag tag) {
        return tagMapper.findByName(tag);
    }

    @Override
    public int add(Tag tag) {
        return tagMapper.add(tag);
    }

    @Override
    public Tag findById(Integer id) {
        return tagMapper.findById(id);
    }

    @Override
    public int update(Tag tag) {
        return tagMapper.update(tag);
    }

    @Override
    public void del(Integer id) {
        tagMapper.del(id);
    }

    @Override
    @Transactional
    public void delList(Integer parentId) throws RuntimeException {
        try {
            tagMapper.delList(parentId);
            tagMapper.del(parentId);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
}
