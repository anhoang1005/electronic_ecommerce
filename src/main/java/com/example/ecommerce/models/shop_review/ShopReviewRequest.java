package com.example.ecommerce.models.shop_review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopReviewRequest {
    private Long id;
    private String shopCode;
    private Integer rating;
    private String comment;
}
