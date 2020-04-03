package com.hzu.community.exceptions;

public class ArticleException extends RuntimeException{
    private static final long serialVersionUID = 7941148128365514007L;
    public ArticleException(String msg){
        super(msg);
    }
}
