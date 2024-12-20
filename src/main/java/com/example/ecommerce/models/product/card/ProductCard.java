package com.example.ecommerce.models.product.card;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCard {
    private Long productId;
    private String productCode;
    private String productTitle;
    private String productSlug;
    private String imageUrl;
    private String breadcrumbs;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Double rating;
    private Integer soldCount;
    private Long brandId;
    private Long categoryId;
    private String brandName;
    private String brand;
    private String category;
}
