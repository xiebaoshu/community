package com.hzu.community.bean;

import java.util.Date;
import java.util.List;

public class LostArticle {
    private Integer lostArticleId;
    private ArticleCategory articleCategory;
    private UserInfo userInfo;
    private Area area;
    private ItemCategory itemCategory;
    private String articleImg;
    private String articleTitle;
    private String phone;
    private String articleContent;
    private String description;
    private Date createTime;
    private Date finishTime;
    private UserInfo finishUser;
    private Integer readCount;
//    一级评论
    private List<Comment> commentList;

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLostArticleId() {
        return lostArticleId;
    }

    public void setLostArticleId(Integer lostArticleId) {
        this.lostArticleId = lostArticleId;
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

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
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

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public UserInfo getFinishUser() {
        return finishUser;
    }

    public void setFinishUser(UserInfo finishUser) {
        this.finishUser = finishUser;
    }
    public String getArticleImg() {
        return articleImg;
    }

    public void setArticleImg(String articleImg) {
        this.articleImg = articleImg;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "LostArticle{" +
                "lostArticleId=" + lostArticleId +
                ", articleCategory=" + articleCategory +
                ", userInfo=" + userInfo +
                ", area=" + area +
                ", itemCategory=" + itemCategory +
                ", articleImg='" + articleImg + '\'' +
                ", articleTitle='" + articleTitle + '\'' +
                ", phone='" + phone + '\'' +
                ", articleContent='" + articleContent + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", finishTime=" + finishTime +
                ", finishUser=" + finishUser +
                ", readCount=" + readCount +
                ", commentList=" + commentList +
                '}';
    }
}
