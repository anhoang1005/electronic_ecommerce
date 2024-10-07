package com.example.ecommerce.exceptions.request;

public class RequestNotFoundException extends RuntimeException{
    public RequestNotFoundException(String s){
        super(s);
    }
}
