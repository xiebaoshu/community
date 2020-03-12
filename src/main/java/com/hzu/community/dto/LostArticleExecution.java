package com.hzu.community.dto;

import com.hzu.community.bean.LostArticle;
import com.hzu.community.enums.LostArticleEnum;

public class LostArticleExecution {
    //结果状态
    private int state;
    //状态标识
    private String stateInfo;
    //店铺数量

    //操作的LostArticle,增删改
    private LostArticle lostArticle;

    public LostArticleExecution() {
    }
    //    LostArticle操作失败构造器，不带数据
    public LostArticleExecution(LostArticleEnum lostArticleEnum) {
        this.state = lostArticleEnum.getState();
        this.stateInfo = lostArticleEnum.getStateInfo();
    }
    //    LostArticle操作成功构造器，带数据
    public LostArticleExecution(LostArticleEnum lostArticleEnum, LostArticle lostArticle) {
        this.state = lostArticleEnum.getState();
        this.stateInfo = lostArticleEnum.getStateInfo();
        this.lostArticle = lostArticle;
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

    public LostArticle getLostArticle() {
        return lostArticle;
    }

    public void setLostArticle(LostArticle lostArticle) {
        this.lostArticle = lostArticle;
    }
}
