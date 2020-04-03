package com.hzu.community.enums;

public enum ArticleEnum {
    SUCCESS(1,"操作成功"),UPDATE_WRONG(-3,"更新失败"),
    NULL_IMG(-1,"ShopId为空"),NULL_Article(-2,"Article信息为空");
    private int state;
    private String stateInfo;
    private ArticleEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }
    public static ArticleEnum stateOf(int state){
        //根据state数据，获取相应LostArticleEnumInfo
        for(ArticleEnum stateEnum : values())
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
