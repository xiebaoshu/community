package com.hzu.community.service.impl;

import com.hzu.community.bean.ItemCategory;
import com.hzu.community.mapper.ItemCategoryMapper;
import com.hzu.community.service.ItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {
    @Autowired
    ItemCategoryMapper itemCategoryMapper;

    @Override
    public ItemCategory findItemCategoryById(Integer id) {
        return itemCategoryMapper.findItemCategoryById(id);
    }

    @Override
    public List<ItemCategory> getItemCategoryist(){
        return itemCategoryMapper.getItemCategoty();
    }
}
