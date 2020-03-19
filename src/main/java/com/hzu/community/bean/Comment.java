package com.hzu.community.bean;

import com.sun.javafx.scene.traversal.ParentTraversalEngine;

import java.util.Date;
import java.util.List;

public class Comment {
    private Long commentId;
    private Integer articleId;
    private UserInfo user;
    private String content;
//    父评论
    private Comment parentComment;
    private boolean isAdmin;
    private Date createTime;
//    子评论列表
    private List<Comment> commentList;
    private UserInfo replyUser;
    private boolean replyIsAdmin;

    public boolean isReplyIsAdmin() {
        return replyIsAdmin;
    }

    public void setReplyIsAdmin(boolean replyIsAdmin) {
        this.replyIsAdmin = replyIsAdmin;
    }

    public UserInfo getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(UserInfo replyUser) {
        this.replyUser = replyUser;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", articleId=" + articleId +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", parentComment=" + parentComment +
                ", isAdmin=" + isAdmin +
                ", createTime=" + createTime +
                ", commentList=" + commentList +
                '}';
    }
}
