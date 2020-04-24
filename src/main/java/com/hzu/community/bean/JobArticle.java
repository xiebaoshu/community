package com.hzu.community.bean;

import org.springframework.web.multipart.MultipartFile;

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
    private Date editTime;
    private Integer readCount;

    //日结还是月结
    private Integer knots;
    private Salary salary;
    private Boolean top;
    private MultipartFile upload;
    //    排序方式
    private String sort;

    public MultipartFile getUpload() {
        return upload;
    }

    public void setUpload(MultipartFile upload) {
        this.upload = upload;
    }

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

    public Boolean getTop() {
        return top;
    }

    public void setTop(Boolean top) {
        this.top = top;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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


                ", salary=" + salary +
                '}';
    }
}
