package com.example.ecommerce.service;

public interface MailService {

    //Gui mail thong thuong
    void sendEmail(String to, String subject, String body);

    //Gui ma xac nhan dang ki tai khoan
    void sendVerifyRegisterEmail(String toAdress, String verifyCode);

    //Gui ma xac nhan quen mat khau
    void sendFogotPasswordMail(String fullName, String email, String verifyCode);
}
