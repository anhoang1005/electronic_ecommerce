package com.example.ecommerce.service;

import com.example.ecommerce.models.users.UserRegisterRequest;
import com.example.ecommerce.payload.JwtData;
import com.example.ecommerce.payload.ResponseBody;

public interface AccountService {

    JwtData loginUsers(String username, String password);

    ResponseBody registerUsers(UserRegisterRequest user);

    ResponseBody checkVerifyCodeRegister(String email, String verifyCode);

    ResponseBody userForgotPassword(String email);

    ResponseBody checkVerifyCodeForgotPassword(String email, String newPassword, String verifyCode);

    ResponseBody generateRootUsers();

}
