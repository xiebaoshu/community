package com.hzu.community.bean;

import java.util.List;

public class ArticleCategory {
    private Integer articleCategoryId;

    private String articleCategoryName;
    private Integer parentId;
    private List<Tag> tagList;

    public Integer getArticleCategoryId() {
        return articleCategoryId;
    }

    public void setArticleCategoryId(Integer articleCategoryId) {
        this.articleCategoryId = articleCategoryId;
    }

    public String getArticleCategoryName() {
        return articleCategoryName;
    }

    public void setArticleCategoryName(String articleCategoryName) {
        this.articleCategoryName = articleCategoryName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    @Override
    public String toString() {
        return "ArticleCategory{" +
                "articleCategoryId=" + articleCategoryId +
                ", articleCategoryName='" + articleCategoryName + '\'' +
                ", parentId=" + parentId +
                ", tagList=" + tagList +
                '}';
    }
}
