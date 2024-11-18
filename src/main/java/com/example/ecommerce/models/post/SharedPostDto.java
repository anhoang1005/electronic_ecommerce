package com.example.ecommerce.models.post;

import com.example.ecommerce.entities.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedPostDto {
    @JsonProperty("shared_post_id")
    private Long sharedPostId;
    @JsonProperty("post_role")
    private String postRole;
    @JsonProperty("shared_post_avatar")
    private String sharedPostAvatar;
    @JsonProperty("shared_post_name")
    private String sharedPostName;
    @JsonProperty("content")
    private String content;
    @JsonProperty("media_url")
    private List<String> mediaUrl;
    @JsonProperty("hash_tag")
    private List<String> hashTag;
    @JsonProperty("created_at")
    private String createdAt;
}
