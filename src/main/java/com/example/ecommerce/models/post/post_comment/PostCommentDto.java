package com.example.ecommerce.models.post.post_comment;

import com.example.ecommerce.entities.enums.BaseRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentDto {
    private Long commentId;
    private String userCode;
    private String shopCode;
    private String userAvatar;
    private String username;
    private BaseRole role;
    private String content;
    private String mediaUrl;
    private Integer lever;
    private String createdAt;
    private List<PostCommentDto> children;
}
