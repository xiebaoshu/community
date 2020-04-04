package com.hzu.community.bean;

import java.util.Date;
import java.util.List;

public class JobArticle {
    private Integer id;
    private ArticleCategory articleCategory;
    private UserInfo userInfo;
    private String articleImg;
    private String articleTitle;
    private String description;
    private String articleContent;
    private String tag;
    private Date createTime;
    private Integer readCount;
    //    一级评论
    private List<Comment> commentList;
    //日结还是月结
    private Integer knots;
    private Salary salary;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getArticleImg() {
        return articleImg;
    }

    public void setArticleImg(String articleImg) {
        this.articleImg = articleImg;
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

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }



    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Integer getKnots() {
        return knots;
    }

    public void setKnots(Integer knots) {
        this.knots = knots;
    }

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "JobArticle{" +
                "id=" + id +
                ", articleCategory=" + articleCategory +
                ", userInfo=" + userInfo +
                ", articleImg='" + articleImg + '\'' +
                ", articleTitle='" + articleTitle + '\'' +
                ", description='" + description + '\'' +
                ", articleContent='" + articleContent + '\'' +
                ", tag='" + tag + '\'' +
                ", createTime=" + createTime +
                ", readCount=" + readCount +
                ", commentList=" + commentList +

                ", salary=" + salary +
                '}';
    }
}
