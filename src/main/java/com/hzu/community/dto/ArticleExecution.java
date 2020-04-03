package com.hzu.community.dto;

import com.hzu.community.enums.ArticleEnum;

public class ArticleExecution<T> {
    //结果状态
    private int state;
    //状态标识
    private String stateInfo;


    //操作的Article,增删改
    private T article;

    public ArticleExecution() {
    }
    //    构造器，不返回数据
    public ArticleExecution(ArticleEnum articleEnum) {
        this.state = articleEnum.getState();
        this.stateInfo = articleEnum.getStateInfo();
    }
    //    LostArticle操作成功构造器，返回数据
    public ArticleExecution(ArticleEnum articleEnum, T article) {
        this.state = articleEnum.getState();
        this.stateInfo = articleEnum.getStateInfo();
        this.article = article;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public T getArticle() {
        return article;
    }

    public void setArticle(T article) {
        this.article = article;
    }
}
