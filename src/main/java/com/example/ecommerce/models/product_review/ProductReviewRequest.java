package com.example.ecommerce.models.product_review;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class ProductReviewRequest {
    private Long reviewId;
    private Integer rating;
    private String textReview;
    private String imageReview;
    private String tag;
}
