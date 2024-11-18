package com.example.ecommerce.service;

import com.example.ecommerce.models.post.post_comment.PostCommentDto;
import com.example.ecommerce.payload.ResponseBody;

public interface PostCommentService {

    ResponseBody userCreateComment(PostCommentDto dto);
    ResponseBody userUpdateComment(PostCommentDto dto);
    ResponseBody userDeleteComment(Long commentId);
    ResponseBody userReactionComment(Long commentId);
}
