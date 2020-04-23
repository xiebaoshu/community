package com.hzu.community.service;

import com.hzu.community.bean.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagService {

//    获取所有标签
    public List<Tag> allTag(@Param("articleParCategory") Integer articleParCategory);
//    获取二级标签
    public List<Tag> tagList(@Param("parentId") Integer parentId);

    public Tag findByName(Tag tag);
    public Tag findById(@Param("id") Integer id);
    public int add(Tag tag);
    public int update(Tag tag);
    //    删除标签
    public void del(Integer id);
    //    删除标签组
    public void delList(Integer parentId) throws RuntimeException;


}
