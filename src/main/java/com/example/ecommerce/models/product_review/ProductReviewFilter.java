package com.example.ecommerce.models.product_review;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class ProductReviewFilter {
    private String productId;
    private Integer rating;
    private Integer sort;
    private Integer pageNumber;
    private Integer pageSize;
}
