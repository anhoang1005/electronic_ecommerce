package com.example.ecommerce.controller.users;

import com.example.ecommerce.models.users.UserRegisterRequest;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/api/all/users/detail")
    public ResponseEntity<ResponseBody> userGetUserDetailApi(
            @RequestParam("email") String email
    ){
        return ResponseEntity.ok(usersService.usersGetUsersDetailResponse(email));
    }

    @PutMapping("/api/all/users/change-password")
    public ResponseEntity<ResponseBody> userChangePasswordApi(
            @RequestParam("email") String email,
            @RequestParam("old_password") String oldPassword,
            @RequestParam("new_password") String newPassword
    ){
        return ResponseEntity.ok(usersService.usersChangePassword(email, oldPassword, newPassword));
    }

    @PutMapping("/api/all/users/change-info")
    public ResponseEntity<ResponseBody> userChangeInfoApi(
            @RequestBody UserRegisterRequest userInfo){
        return ResponseEntity.ok(usersService.usersChangeUserInfo(userInfo));
    }

    @PutMapping("/api/all/users/change-avatar")
    public ResponseEntity<ResponseBody> userChangeImageUrlApi(
            @RequestParam("email") String email,
            @RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(usersService.usersChangeAvatar(email, file));
    }
}
