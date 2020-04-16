package com.hzu.community.dto;

import com.hzu.community.bean.Nav;
import com.hzu.community.enums.NavEnum;

public class NavExecution {
    //结果状态
    private int state;
    //状态标识
    private String stateInfo;


    //操作的Nav,增删改
    private Nav nav;

    public NavExecution() {
    }
    //    构造器，不返回数据
    public NavExecution(NavEnum navEnum) {
        this.state = navEnum.getState();
        this.stateInfo = navEnum.getStateInfo();
    }
    //    Nav操作成功构造器，返回数据
    public NavExecution(NavEnum navEnum,Nav nav) {
        this.state = navEnum.getState();
        this.stateInfo = navEnum.getStateInfo();
        this.nav = nav;
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

    public Nav getNav() {
        return nav;
    }

    public void setNav(Nav nav) {
        this.nav = nav;
    }
}
