package com.hzu.community.exceptions;

public class UserException extends RuntimeException {

    private static final long serialVersionUID = -6198627187438059845L;

    public UserException(String msg){
        super(msg);
    }

}
