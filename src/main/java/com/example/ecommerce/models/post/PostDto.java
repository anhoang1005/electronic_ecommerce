package com.example.ecommerce.models.post;

import com.example.ecommerce.entities.Post;
import com.example.ecommerce.entities.PostReaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    @JsonProperty("post_id")
    private Long id;
    @JsonProperty("is_shared")
    private Boolean isShared;
    @JsonProperty("post_role")
    private String postRole;
    @JsonProperty("post_avatar")
    private String postAvatar;
    @JsonProperty("post_name")
    private String postName;
    @JsonProperty("content")
    private String content;
    @JsonProperty("visibility")
    private Post.Visibility visibility;
    @JsonProperty("media_url")
    private List<String> mediaUrl;
    @JsonProperty("location")
    private String location;
    @JsonProperty("hash_tag")
    private List<String> hashTag;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("my_reaction")
    private PostReaction.Reaction myReaction;
    @JsonProperty("like_count")
    private Long likeCount;
    @JsonProperty("comment_count")
    private Long commentCount;
    @JsonProperty("shared_count")
    private Long sharedCount;
    @JsonProperty("shared_post")
    private SharedPostDto sharedPost;
    @JsonProperty("created_at")
    private String createdAt;
}
