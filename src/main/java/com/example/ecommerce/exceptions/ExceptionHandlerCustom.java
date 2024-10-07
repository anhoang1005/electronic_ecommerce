package com.example.ecommerce.exceptions;

import com.example.ecommerce.exceptions.external.FileFailedUploadException;
import com.example.ecommerce.exceptions.external.SendMailFailedException;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.exceptions.users.AccountLockedException;
import com.example.ecommerce.exceptions.users.InvalidCredentialsException;
import com.example.ecommerce.exceptions.users.UnauthorizedException;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerCustom extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UnauthorizedException.class, InvalidCredentialsException.class})
    public ResponseEntity<Object> unauthorizedException(RuntimeException e) {
        //e.printStackTrace();
        ResponseBody responseBody = new ResponseBody(
                null,
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.UNAUTHORIZED_REQUEST
        );
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @ExceptionHandler({AccountLockedException.class})
    public ResponseEntity<Object> accountLockedException(RuntimeException e){
        ResponseBody responseBody = new ResponseBody(
                null,
                ResponseBody.Status.SUCCESS,
                "Locked Account",
                ResponseBody.Code.UNAUTHORIZED_REQUEST
        );
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @ExceptionHandler({FileFailedUploadException.class, RequestNotFoundException.class})
    public ResponseEntity<Object> badRequestException(RuntimeException e){
        ResponseBody responseBody = new ResponseBody(
                null,
                ResponseBody.Status.SUCCESS,
                e.getMessage(),
                ResponseBody.Code.FORBIDDEN
        );
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @ExceptionHandler({SendMailFailedException.class})
    public ResponseEntity<Object> mailException(RuntimeException e){
        ResponseBody responseBody = new ResponseBody(
                null,
                ResponseBody.Status.SUCCESS,
                "Send Mail Failed",
                ResponseBody.Code.NOT_FOUND
        );
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
