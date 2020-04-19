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
    private Date editTime;
    private String articleImg;
    private Boolean top;
    private Boolean lookMe;
    private Integer date;

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

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getArticleImg() {
        return articleImg;
    }

    public void setArticleImg(String articleImg) {
        this.articleImg = articleImg;
    }

    public Boolean getTop() {
        return top;
    }

    public void setTop(Boolean top) {
        this.top = top;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Boolean getLookMe() {
        return lookMe;
    }

    public void setLookMe(Boolean lookMe) {
        this.lookMe = lookMe;
    }

    @Override
    public String toString() {
        return "SearchDto{" +
                "id=" + id +
                ", articleCategory=" + articleCategory +
                ", articleTitle='" + articleTitle + '\'' +
                ", description='" + description + '\'' +
                ", userInfo=" + userInfo +
                ", edit_time=" + editTime +
                ", article_img='" + articleImg + '\'' +
                '}';
    }
}
