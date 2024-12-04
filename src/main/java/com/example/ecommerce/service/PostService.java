package com.example.ecommerce.service;

import com.example.ecommerce.entities.Post;
import com.example.ecommerce.entities.PostComment;
import com.example.ecommerce.entities.PostHashTag;
import com.example.ecommerce.entities.PostReaction;
import com.example.ecommerce.models.post.PostRequest;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostHashTag userCreateHashTag(String hashtag);
    ResponseBody userCreatePost(PostRequest req, List<MultipartFile> listFile);
    ResponseBody userUpdatePost(PostRequest req, List<MultipartFile> listFile);
    ResponseBody userDeletePost(Long postId);
    ResponseBody userSharedPost(PostRequest req);
    ResponseBody userGetTheirListPost(String postHashtag, int pageNumber, int pageSize);
    ResponseBody userGetUserPostOther(String userCode, String hashTag, int pageNumber, int pageSize);
    ResponseBody adminActivePost(Long postId, Boolean status);
    ResponseBody useReactionPost(Long postId, PostReaction.Reaction reaction);
}
