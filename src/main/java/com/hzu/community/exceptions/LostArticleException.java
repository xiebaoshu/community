package com.hzu.community.exceptions;

public class LostArticleException extends RuntimeException{
    private static final long serialVersionUID = 7941148128365514007L;
    public LostArticleException(String msg){
        super(msg);
    }
}
