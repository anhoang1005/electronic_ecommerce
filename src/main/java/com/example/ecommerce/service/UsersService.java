package com.example.ecommerce.service;

import com.example.ecommerce.models.users.UserRegisterRequest;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface UsersService {

    ResponseBody usersChangePassword(String email, String oldPassword, String newPassword);

    ResponseBody usersChangeUserInfo(UserRegisterRequest req);

    ResponseBody usersChangeAvatar(String email, MultipartFile file);

    ResponseBody usersGetUsersDetailResponse(String email);

}
