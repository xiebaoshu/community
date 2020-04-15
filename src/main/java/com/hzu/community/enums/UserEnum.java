package com.hzu.community.enums;

public enum UserEnum {
    SUCCESS(1,"操作成功"),UPDATE_WRONG(-3,"更新失败"),
    NULL_IMG(-1,"userId为空"),NULL_User(-2,"User信息为空");
    private int state;
    private String stateInfo;
    private UserEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }
    public static UserEnum stateOf(int state){
        //根据state数据，获取相应LostUserEnumInfo
        for(UserEnum stateEnum : values())
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
