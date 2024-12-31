package com.example.ecommerce.models.product_review;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewRating {
    private Long review_count;
    private Double rating;
    private Long one_star_count;
    private Long two_star_count;
    private Long three_star_count;
    private Long four_star_count;
    private Long five_star_count;
}
