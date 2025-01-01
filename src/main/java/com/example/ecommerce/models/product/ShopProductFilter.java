package com.example.ecommerce.models.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopProductFilter {
    private String shopCode;
    private String title;
    private Long brandId;
    private String category;
    private String shopCategoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer rating;
    private String sort;
    private int pageNumber;
    private int pageSize;
}
