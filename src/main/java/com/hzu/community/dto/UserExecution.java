package com.hzu.community.dto;

import com.hzu.community.bean.UserInfo;
import com.hzu.community.enums.UserEnum;

public class UserExecution {
    //结果状态
    private int state;
    //状态标识
    private String stateInfo;


    //操作的UserInfo,增删改
    private UserInfo user;

    public UserExecution() {
    }
    //    构造器，不返回数据
    public UserExecution(UserEnum userEnum) {
        this.state = userEnum.getState();
        this.stateInfo = userEnum.getStateInfo();
    }
    //    UserInfo操作成功构造器，返回数据
    public UserExecution(UserEnum userEnum, UserInfo user) {
        this.state = userEnum.getState();
        this.stateInfo = userEnum.getStateInfo();
        this.user = user;
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

    public UserInfo getUserInfo() {
        return user;
    }

    public void setUserInfo(UserInfo user) {
        this.user = user;
    }
}
