package com.example.ecommerce.service;

import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.models.post.PostRequest;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    ResponseBody userCreatePost(PostRequest req, List<MultipartFile> listFile);
    ResponseBody userUpdatePost(PostRequest req, List<MultipartFile> listFile);
    ResponseBody userDeletePost(Long postId);
    ResponseBody userSharedPost(PostRequest req, Long sharedId);
    ResponseBody userGetTheirListPost();
}
