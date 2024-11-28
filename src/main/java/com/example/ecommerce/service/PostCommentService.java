package com.example.ecommerce.service;

import com.example.ecommerce.models.post.post_comment.PostCommentRequest;
import com.example.ecommerce.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface PostCommentService {

    ResponseBody userGetListComment(Long postId, Long commentId, int pageNumber);
    ResponseBody userCreateComment(PostCommentRequest dto, MultipartFile file);
    ResponseBody userUpdateComment(PostCommentRequest dto, MultipartFile file);
    ResponseBody userDeleteComment(Long commentId);
    ResponseBody userReactionComment(Long commentId);
}
