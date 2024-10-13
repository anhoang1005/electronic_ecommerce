package com.example.ecommerce.models.shop_review;

import lombok.Data;

@Data
public class ShopReviewFilter {
    private String shopCode;
    private Integer rating;
    private Integer sort;
    private Integer pageNumber;
}
