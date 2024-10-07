package com.example.ecommerce.exceptions.external;

public class SendMailFailedException extends RuntimeException{
    public SendMailFailedException(String s){
        super(s);
    }
}
