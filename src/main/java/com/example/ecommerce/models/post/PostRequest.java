package com.example.ecommerce.models.post;

import com.example.ecommerce.entities.Post;
import com.example.ecommerce.entities.enums.BaseRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
    private Long id;
    private BaseRole postRole;
    private Post.Visibility visibility;
    private String content;
    private Boolean isPrivate;
    private String location;
    private List<String> hashTag;
    private String active;
}
