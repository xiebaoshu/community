package com.hzu.community.enums;

public enum LostArticleEnum {
    SUCCESS(1,"操作成功"),
    NULL_IMG(-1,"ShopId为空"),NULL_Article(-2,"Article信息为空");
    private int state;
    private String stateInfo;
    private LostArticleEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }
    public static LostArticleEnum stateOf(int state){
        //根据state数据，获取相应LostArticleEnumInfo
        for(LostArticleEnum stateEnum : values())
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
