package com.example.ecommerce.service.implement;

import com.example.ecommerce.models.post.post_comment.PostCommentDto;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.PostCommentRepository;
import com.example.ecommerce.repository.PostRepository;
import com.example.ecommerce.service.PostCommentService;
import com.example.ecommerce.utils.AuthenticationUtils;
import com.example.ecommerce.utils.DateTimeMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class IPostCommentService implements PostCommentService {
    private final PostCommentRepository pcmRepository;
    private final AuthenticationUtils authUtils;
    private final DateTimeMapperUtils timeUtils;
    private final PostRepository postRepository;


    @Override
    public ResponseBody userCreateComment(PostCommentDto dto) {
        return null;
    }

    @Override
    public ResponseBody userUpdateComment(PostCommentDto dto) {
        return null;
    }

    @Override
    public ResponseBody userDeleteComment(Long commentId) {
        return null;
    }

    @Override
    public ResponseBody userReactionComment(Long commentId) {
        return null;
    }
}
