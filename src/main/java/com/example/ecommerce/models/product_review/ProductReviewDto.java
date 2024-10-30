package com.example.ecommerce.models.product_review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@Builder
public class ProductReviewDto {
    @JsonProperty("review_id")
    private Long reviewId;
    @JsonProperty("user_avatar")
    private String userAvatar;
    @JsonProperty("username")
    private String username;
    @JsonProperty("rating")
    private Integer rating;
    @JsonProperty("text_comment")
    private String textComment;
    @JsonProperty("image_comment")
    private String imageComment;
    @JsonProperty("tag")
    private List<String> tag;
    @JsonProperty("is_shop_reply")
    private boolean isShopReply;
    @JsonProperty("shop_reply")
    private String shopReply;
    @JsonProperty("like_count")
    private Integer likeCount;
    @JsonProperty("createdAt")
    private String createdAt;
}
