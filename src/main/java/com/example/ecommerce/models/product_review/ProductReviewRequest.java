package com.example.ecommerce.models.product_review;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewRequest {
    private Long reviewId;
    private Long productId;
    private Integer rating;
    private String textReview;
    private String imageReview;
    private String tag;
}
