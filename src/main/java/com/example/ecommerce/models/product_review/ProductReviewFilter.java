package com.example.ecommerce.models.product_review;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewFilter {
    private Long productId;
    private Integer rating;
    private Integer sort;
    private Integer pageNumber;
    private Integer pageSize;
}
