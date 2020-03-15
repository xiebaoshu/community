package com.hzu.community.mapper;

import com.hzu.community.bean.ArticleCategory;
import com.hzu.community.bean.ItemCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ItemCategoryMapper {
    @Select("select * from item_category where item_category_id=#{id}")
    public ItemCategory findItemCategoryById(@Param("id") int id);

    @Select("select * from item_category")
    public List<ItemCategory> getItemCategoty();


}
