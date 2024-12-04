package com.example.ecommerce.models.post.post_comment;

import com.example.ecommerce.entities.enums.BaseRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentRequest {
    private Long commentId;
    private Long postId;
    private Long parentId;
    private BaseRole role;
    private String content;
}
