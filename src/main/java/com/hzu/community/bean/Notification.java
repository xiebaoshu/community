package com.hzu.community.bean;

import java.util.Date;

public class Notification {
    private Long id;
//    信息发出者
    private UserInfo notifier;
    //    信息接受者
    private UserInfo receiver;
//    该消息所在的文章id
    private Integer articleId;
//    该消息所在的文章的类别
    private Integer articleParCategory;
//    该消息类型（回复评论，回复文章）
    private Integer type;
    private Date createTime;
    private String outerTitle;
    private boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserInfo getNotifier() {
        return notifier;
    }

    public void setNotifier(UserInfo notifier) {
        this.notifier = notifier;
    }

    public UserInfo getReceiver() {
        return receiver;
    }

    public void setReceiver(UserInfo receiver) {
        this.receiver = receiver;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getArticleParCategory() {
        return articleParCategory;
    }

    public void setArticleParCategory(Integer articleParCategory) {
        this.articleParCategory = articleParCategory;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOuterTitle() {
        return outerTitle;
    }

    public void setOuterTitle(String outerTitle) {
        this.outerTitle = outerTitle;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
