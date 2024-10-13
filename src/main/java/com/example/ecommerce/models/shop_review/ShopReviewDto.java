package com.example.ecommerce.models.shop_review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopReviewDto {
    private Long id;
    private Integer rating;
    private String comment;
    private Boolean isReply;
    private String reply;
    private Boolean active;
}
