package com.projects.coaching_offline_support.common.Exceptions;

public class UserAlreadyExistsException extends  RuntimeException{

    public UserAlreadyExistsException(String message){
        super(message);
    }


}
