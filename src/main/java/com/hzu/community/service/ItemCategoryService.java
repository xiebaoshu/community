package com.hzu.community.service;



import com.hzu.community.bean.ItemCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemCategoryService {
    public List<ItemCategory> getItemCategoryist();
    public ItemCategory findItemCategoryById(@Param("id") Integer id);
}
