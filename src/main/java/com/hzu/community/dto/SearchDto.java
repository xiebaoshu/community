package com.hzu.community.dto;

import com.hzu.community.bean.ArticleCategory;
import com.hzu.community.bean.UserInfo;
import org.apache.catalina.User;

import java.util.Date;

public class SearchDto {
    private Integer id;
    private ArticleCategory articleCategory;
    private String articleTitle;
    private String description;
    private UserInfo userInfo;
    private Date createTime;
    private String articleImg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArticleCategory getArticleCategory() {
        return articleCategory;
    }

    public void setArticleCategory(ArticleCategory articleCategory) {
        this.articleCategory = articleCategory;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getArticleImg() {
        return articleImg;
    }

    public void setArticleImg(String articleImg) {
        this.articleImg = articleImg;
    }

    @Override
    public String toString() {
        return "SearchDto{" +
                "id=" + id +
                ", articleCategory=" + articleCategory +
                ", articleTitle='" + articleTitle + '\'' +
                ", description='" + description + '\'' +
                ", userInfo=" + userInfo +
                ", create_time=" + createTime +
                ", article_img='" + articleImg + '\'' +
                '}';
    }
}
