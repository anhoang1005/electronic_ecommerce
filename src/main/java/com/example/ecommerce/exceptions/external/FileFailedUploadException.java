package com.example.ecommerce.exceptions.external;

public class FileFailedUploadException extends RuntimeException{
    public FileFailedUploadException(String message){
        super(message);
    }
}
