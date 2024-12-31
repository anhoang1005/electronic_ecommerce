package com.example.ecommerce.models.product_review;

import com.example.ecommerce.payload.PageData;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewResponse {
    private ProductReviewDto my_review;
    private PageData review_page;
    private ProductReviewRating review_rating;
}
