package com.hzu.community.enums;

public enum  NavEnum {
    SUCCESS(1,"操作成功"),UPDATE_WRONG(-3,"更新失败"),
    NULL_Url(-1,"网址为空"),NULL_Name(-2,"网站名称为空"),NULL_Nav(-3,"NAv为空"),REPEACT_NAV(-4,"该导航已存在，请不要重复添加");
    private int state;
    private String stateInfo;
    private NavEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }
    public static NavEnum stateOf(int state){
        //根据state数据，获取相应NavEnumInfo
        for(NavEnum stateEnum : values())
            if(stateEnum.getState() == state){
                return stateEnum;
            }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
